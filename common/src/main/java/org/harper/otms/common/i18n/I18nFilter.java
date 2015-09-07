package org.harper.otms.common.i18n;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.LocaleUtils;
import org.apache.velocity.VelocityContext;
import org.harper.otms.common.mail.SharedVelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class I18nFilter implements Filter {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private String propertyPath;

	private Map<String, Properties> props;

	@Override
	public void init(FilterConfig config) throws ServletException {
		this.propertyPath = config.getInitParameter("propertyPath");
		this.props = new HashMap<String, Properties>();
	}

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;

		String lang = req.getParameter("lang");
		if (StringUtils.isEmpty(lang)) {
			Object langObj = req.getSession(true).getAttribute("lang");
			if (langObj != null) {
				lang = String.valueOf(langObj);
			}
			if (StringUtils.isEmpty(lang))
				lang = "";
		} else {
			req.getSession(true).setAttribute("lang", lang);
		}

		HttpServletResponse resp = (HttpServletResponse) response;
		BufferedHttpServletResponseWrapper wrapper = new BufferedHttpServletResponseWrapper(
				resp);
		chain.doFilter(request, wrapper);
		wrapper.getWriter().flush();
		wrapper.getOutputStream().flush();

		if (wrapper.getContentType().startsWith("text/html")) {
			try {
				// Translate the content of html text
				byte[] content = wrapper.getBufferContent();

				String[] paths = req.getRequestURI().split("/");
				String pageName = paths[paths.length - 1].split("\\.")[0];

				Properties prop = getProperties(pageName, lang);

				if (null == prop) {
					response.getOutputStream().write(content);
				} else {
					// Pass current language information to client
					prop.put("lang", lang);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					PrintWriter buffer = new PrintWriter(baos);
					SharedVelocityEngine.get().evaluate(
							new VelocityContext(prop), buffer, pageName,
							new String(content, "utf8"));
					buffer.flush();
					buffer.close();
					byte[] newdata = baos.toByteArray();
					resp.setContentLength(newdata.length);
					resp.getOutputStream().write(newdata);
				}
			} catch (Exception e) {
				logger.error("Failed to translate webpage", e);
				response.getOutputStream().write(wrapper.getBufferContent());
			}
		} else {
			response.getOutputStream().write(wrapper.getBufferContent());
		}
	}

	private Properties getProperties(String pageName, String lang) {
		String propkey = pageName;
		if (!StringUtils.isEmpty(lang)) {
			propkey = MessageFormat.format("{0}_{1}", pageName, lang);
		}
		String propFilePath = MessageFormat.format("{0}{1}{2}",
				this.propertyPath, File.separator, pageName);
		if (!props.containsKey(propkey)) {
			ResourceBundle rb = null;
			if (StringUtils.isEmpty(lang)) {
				rb = ResourceBundle.getBundle(propFilePath);
			} else {
				rb = ResourceBundle.getBundle(propFilePath,
						LocaleUtils.toLocale(lang));
			}
			if (null == rb) {
				return null;
			}
			Properties prop = new Properties();
			for (String key : rb.keySet()) {
				prop.setProperty(key, rb.getString(key));
			}
			props.put(propkey, prop);
		}
		return props.get(propkey);
	}
}

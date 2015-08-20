package org.harper.otms.common.servlet;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.harper.otms.common.dto.RequestDto;
import org.harper.otms.common.spring.AppContextAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;

/**
 * This upload servlet accept a POST request, extract the file information and
 * invoke a service method based on function name. It is an alternative entry to
 * DWR file upload in the case that DWR doesn't work with Chrome. (DWR-333)
 * 
 * After processing can be done by injecting function handler.
 * 
 * @author harper
 * 
 */
public class UploadFileServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5477404991946903316L;

	public static String SERVICE_NAME = "serviceName";

	public static String FUNCTION_NAME = "functionName";

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();

		// Set factory constraints
		// TODO Use servlet parameters
		factory.setSizeThreshold(1024000);
		factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// Set overall request size constraint
		upload.setSizeMax(2048000);

		// Parse the request
		List<FileItem> items;
		try {
			items = upload.parseRequest(req);
		} catch (FileUploadException e) {
			logger.warn(e.getMessage(), e);
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		// Extract data
		Map<String, Object> values = new HashMap<String, Object>();

		Iterator<FileItem> iter = items.iterator();
		while (iter.hasNext()) {
			FileItem item = iter.next();
			if (item.isFormField())
				values.put(item.getFieldName(), item.getString());
			else
				values.put(item.getFieldName(), item.getInputStream());
		}
		// Invoke Service Method
		String serviceName = (String) values.get(SERVICE_NAME);
		String functionName = (String) values.get(FUNCTION_NAME);
		if (StringUtils.isEmpty(serviceName)
				|| StringUtils.isEmpty(functionName)) {
			logger.warn("No service name or function name provided to UploadFileServlet");
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		Object bean = AppContextAccessor.getContext().getBean(serviceName);

		// Find the method and construct DTO
		// For simplicity, nested DTO is not supported here
		for (Method method : bean.getClass().getMethods()) {
			if (method.getName().equals(functionName)) {
				Class<?> dtoClass = method.getParameterTypes()[0];

				try {
					Object dto = dtoClass.newInstance();
					if (dto instanceof RequestDto) {
						BeanUtils.populate(dto, values);
						Object respdto = method.invoke(bean, dto);

						// Convert the response to Json and send back
						String respString = new Gson().toJson(respdto);
						if (logger.isDebugEnabled()) {
							logger.debug(MessageFormat.format(
									"Resp data for {0}.{1}:{2}", serviceName,
									functionName, respString));
						}
						resp.setStatus(HttpServletResponse.SC_OK);
						resp.getOutputStream().print(respString);
						return;
					} else {
						throw new IllegalArgumentException(
								"Input parameter is Not a RequestDto");
					}
				} catch (Exception e) {
					logger.warn("Exception in calling service method", e);
					resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					return;
				}
			}
		}
		logger.warn(serviceName + "." + functionName + " not found");
		resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		return;
	}
}

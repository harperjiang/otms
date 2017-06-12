package org.harper.otms.auth.service.util;

import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CaptchaUtil {

	private static String SECRET_KEY = "6LcVwCQUAAAAAO5w76MF6dM6mmvonHsWv2jzvfBD";

	private static String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

	private static HttpClient httpClient;

	private static JsonParser parser;

	private static int timeout = 2000;

	static {
		RequestConfig.Builder requestBuilder = RequestConfig.custom().setConnectTimeout(timeout)
				.setConnectionRequestTimeout(timeout).setSocketTimeout(timeout);
		httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestBuilder.build()).build();
		parser = new JsonParser();
	}

	public static boolean verify(String input, String ip) {
		try {
			URIBuilder builder = new URIBuilder(VERIFY_URL);
			builder.setParameter("secret", SECRET_KEY).setParameter("response", input).setParameter("remoteip", ip);
			HttpPost post = new HttpPost(builder.build());

			HttpResponse resp = httpClient.execute(post);

			if (resp.getStatusLine().getStatusCode() == 200) {
				JsonObject result = parser.parse(new InputStreamReader(resp.getEntity().getContent()))
						.getAsJsonObject();
				return result.get("success").getAsBoolean();
			}
			return false;
		} catch (Exception e) {
			return false;
		}

	}
}

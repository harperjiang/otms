package org.harper.otms.auth.external.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.KeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.harper.otms.auth.external.TokenProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GoogleTokenProcessor extends TokenProcessor {

	private JsonParser parser = new JsonParser();

	private String appId;

	private Map<String, JsonObject> publicKeys;

	private static String appIdFileName = "appId.json";

	private static String publicKeyLocation = "https://www.googleapis.com/oauth2/v3/certs";

	private HttpClient defaultClient;

	private Logger logger = LoggerFactory.getLogger(getClass());

	public GoogleTokenProcessor() {
		super();
		Reader jsonReader = new InputStreamReader(this.getClass()
				.getResourceAsStream(appIdFileName));
		JsonObject data = parser.parse(jsonReader).getAsJsonObject();
		try {
			jsonReader.close();
		} catch (IOException e) {

		}
		this.appId = data.get("appId").getAsString();

		publicKeys = new HashMap<String, JsonObject>();

		// Request public key from the given address
		defaultClient = HttpClientBuilder.create().build();

		refreshPublicKeys();
	}

	@Override
	public String process(String token) {
		GoogleToken gtoken = parseToken(token);
		if (gtoken == null)
			return null;

		if (!("accounts.google.com".equals(gtoken.getPayload().get("iss")
				.getAsString()) || "https://accounts.google.com".equals(gtoken
				.getPayload().get("iss").getAsString()))) {
			return null;
		}
		if (!appId.equals(gtoken.getPayload().get("aud").getAsString())) {
			return null;
		}
		return gtoken.getPayload().get("sub").getAsString();
	}

	private GoogleToken parseToken(String token) {
		try {
			String[] parts = token.split("\\.");

			GoogleToken gtoken = new GoogleToken();

			byte[] header = Base64.getUrlDecoder().decode(parts[0]);
			byte[] payload = Base64.getUrlDecoder().decode(parts[1]);
			byte[] signature = Base64.getUrlDecoder().decode(parts[2]);
			byte[] content = (parts[0] + "." + parts[1]).getBytes("utf8");

			gtoken.setHeader(parser.parse(new String(header, "utf8"))
					.getAsJsonObject());
			gtoken.setPayload(parser.parse(new String(payload, "utf8"))
					.getAsJsonObject());

			JsonObject key = publicKeys.get(gtoken.getHeader().get("kid")
					.getAsString());
			BigInteger n = bigEndianInteger(Base64.getUrlDecoder().decode(
					key.get("n").getAsString()));
			BigInteger e = bigEndianInteger(Base64.getUrlDecoder().decode(
					key.get("e").getAsString()));
			Signature sig = Signature.getInstance("SHA256withRSA");

			KeySpec keySpec = new RSAPublicKeySpec(n, e);

			PublicKey pub = KeyFactory.getInstance("RSA").generatePublic(
					keySpec);
			sig.initVerify(pub);
			sig.update(content);
			return sig.verify(signature) ? gtoken : null;
		} catch (Exception e) {
			return null;
		}
	}

	protected static BigInteger bigEndianInteger(byte[] input) {
		StringBuilder sb = new StringBuilder();
		for (byte b : input) {
			int value = b;
			if (value < 0)
				value += 256;
			String comp = Integer.toString(value, 16);
			if (comp.length() == 1) {
				sb.append("0");
			}
			sb.append(comp);
		}
		return new BigInteger(sb.toString(), 16);
	}

	protected static class GoogleToken {
		private JsonObject header;
		private JsonObject payload;

		public JsonObject getHeader() {
			return header;
		}

		public void setHeader(JsonObject header) {
			this.header = header;
		}

		public JsonObject getPayload() {
			return payload;
		}

		public void setPayload(JsonObject payload) {
			this.payload = payload;
		}

	}

	private void refreshPublicKeys() {
		publicKeys.clear();
		try {
			HttpResponse resp = defaultClient.execute(new HttpGet(
					publicKeyLocation));
			if (resp.getStatusLine().getStatusCode() == 200) {
				JsonObject data = parser.parse(
						new InputStreamReader(resp.getEntity().getContent()))
						.getAsJsonObject();
				for (JsonElement key : data.get("keys").getAsJsonArray()) {
					JsonObject jobj = key.getAsJsonObject();
					publicKeys.put(jobj.get("kid").getAsString(), jobj);
				}
			}
		} catch (Exception e) {
			logger.warn("Failed to refresh google public key");
		}

	}
}

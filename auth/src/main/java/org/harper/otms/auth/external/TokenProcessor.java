package org.harper.otms.auth.external;

import org.harper.otms.auth.external.impl.GoogleTokenProcessor;

public abstract class TokenProcessor {

	public abstract String process(String token);

	private static GoogleTokenProcessor google = new GoogleTokenProcessor();

	public static TokenProcessor getInstance(ExternalSystem system) {
		switch (system) {
		case Google:
			return google;
		default:
			return null;
		}
	}
}

package org.harper.otms.auth.service;

public interface TokenManager {

	public String acquireToken(int userId);

	public boolean checkToken(int userId, String token);
}

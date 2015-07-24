package org.harper.otms.common.dto;

public class RequestDto extends Dto {

	// Token information

	private int currentUser;

	private String token;

	public int getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(int currentUser) {
		this.currentUser = currentUser;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean needValidation() {
		return true;
	}
}

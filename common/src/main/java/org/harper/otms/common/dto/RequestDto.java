package org.harper.otms.common.dto;


public class RequestDto extends Dto {

	// Token information

	private int currentUser = 1;

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

}

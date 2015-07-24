package org.harper.otms.auth.service.dto;

import org.harper.otms.common.dto.RequestDto;

public class LoginRequestDto extends RequestDto {

	private String username;

	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}

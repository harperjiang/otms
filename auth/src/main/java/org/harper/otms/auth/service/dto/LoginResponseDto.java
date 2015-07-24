package org.harper.otms.auth.service.dto;

import org.harper.otms.common.dto.ResponseDto;

public class LoginResponseDto extends ResponseDto {

	private int userId;

	private String username;

	private String type;

	private String token;

	public LoginResponseDto() {
		super();
	}

	public LoginResponseDto(int errcode) {
		super(errcode);
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}

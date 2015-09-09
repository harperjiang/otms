package org.harper.otms.auth.service.dto;

import org.harper.otms.common.dto.RequestDto;

public class LoginRequestDto extends RequestDto {

	private String username;

	private String password;

	private boolean linkLogin;

	private String sourceSystem;

	private String sourceId;

	private String captcha;

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

	public boolean isLinkLogin() {
		return linkLogin;
	}

	public void setLinkLogin(boolean linkLogin) {
		this.linkLogin = linkLogin;
	}

	public String getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

}

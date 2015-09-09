package org.harper.otms.auth.service.dto;

import org.harper.otms.common.dto.RequestDto;

public class ReqResetPassDto extends RequestDto {

	private String username;

	private String captcha;

	@Override
	public boolean needValidation() {
		return false;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

}

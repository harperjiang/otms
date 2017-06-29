package org.harper.otms.profile.service.dto;

import org.harper.otms.auth.service.dto.CreateUserDto;

public class RegisterUserDto extends CreateUserDto {

	private String captcha;

	@Override
	public boolean needValidation() {
		return false;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

}

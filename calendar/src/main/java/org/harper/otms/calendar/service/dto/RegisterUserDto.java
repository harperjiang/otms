package org.harper.otms.calendar.service.dto;

import org.harper.otms.auth.service.dto.CreateUserDto;

public class RegisterUserDto extends CreateUserDto {

	@Override
	public boolean needValidation() {
		return false;
	}
}

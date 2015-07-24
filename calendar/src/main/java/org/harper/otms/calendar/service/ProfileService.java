package org.harper.otms.calendar.service;

import org.harper.otms.calendar.service.dto.RegisterUserDto;
import org.harper.otms.calendar.service.dto.RegisterUserResponseDto;

public interface ProfileService {

	public RegisterUserResponseDto registerUser(RegisterUserDto request);
}

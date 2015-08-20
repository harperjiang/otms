package org.harper.otms.auth.service;

import org.harper.otms.auth.service.dto.ActivateUserDto;
import org.harper.otms.auth.service.dto.ActivateUserResponseDto;
import org.harper.otms.auth.service.dto.CreateUserDto;
import org.harper.otms.auth.service.dto.CreateUserResponseDto;

public interface UserService {

	CreateUserResponseDto createUser(CreateUserDto request);
	
	ActivateUserResponseDto activateUser(ActivateUserDto request);
}

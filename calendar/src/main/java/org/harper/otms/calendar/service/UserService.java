package org.harper.otms.calendar.service;

import org.harper.otms.calendar.service.dto.CreateUserDto;
import org.harper.otms.calendar.service.dto.CreateUserResponseDto;

public interface UserService {

	CreateUserResponseDto createUser(CreateUserDto request);
}

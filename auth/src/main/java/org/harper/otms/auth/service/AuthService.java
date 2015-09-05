package org.harper.otms.auth.service;

import org.harper.otms.auth.service.dto.LoginRequestDto;
import org.harper.otms.auth.service.dto.LoginResponseDto;

public interface AuthService {

	public LoginResponseDto login(LoginRequestDto request);

}

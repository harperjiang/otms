package org.harper.otms.auth.service;

import org.harper.otms.auth.service.dto.ConfirmResetPassDto;
import org.harper.otms.auth.service.dto.ConfirmResetPassResponseDto;
import org.harper.otms.auth.service.dto.LoginRequestDto;
import org.harper.otms.auth.service.dto.LoginResponseDto;
import org.harper.otms.auth.service.dto.ReqResetPassDto;
import org.harper.otms.auth.service.dto.ReqResetPassResponseDto;

public interface AuthService {

	public LoginResponseDto login(LoginRequestDto request);

	public ReqResetPassResponseDto reqResetPass(ReqResetPassDto request);
	
	public ConfirmResetPassResponseDto confirmResetPass(ConfirmResetPassDto request);
}

package org.harper.otms.auth.service.impl;

import org.harper.otms.auth.dao.UserDao;
import org.harper.otms.auth.entity.User;
import org.harper.otms.auth.service.AuthService;
import org.harper.otms.auth.service.ErrorCode;
import org.harper.otms.auth.service.TokenManager;
import org.harper.otms.auth.service.dto.LoginRequestDto;
import org.harper.otms.auth.service.dto.LoginResponseDto;

public class DefaultAuthService implements AuthService {

	@Override
	public LoginResponseDto login(LoginRequestDto request) {
		User user = getUserDao().findByName(request.getUsername());
		if (null == user) {
			return new LoginResponseDto(ErrorCode.USER_NAME_NOTEXIST);
		}
		LoginResponseDto response = new LoginResponseDto();
		response.setUserId(user.getId());
		response.setType(user.getType());
		response.setUsername(user.getName());
		response.setToken(getTokenManager().acquireToken(user.getId()));

		return response;
	}

	private UserDao userDao;

	private TokenManager tokenManager;

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public TokenManager getTokenManager() {
		return tokenManager;
	}

	public void setTokenManager(TokenManager tokenManager) {
		this.tokenManager = tokenManager;
	}

}

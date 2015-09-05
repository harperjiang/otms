package org.harper.otms.auth.service.impl;

import org.harper.otms.auth.dao.UserDao;
import org.harper.otms.auth.entity.User;
import org.harper.otms.auth.external.ExternalSystem;
import org.harper.otms.auth.external.TokenProcessor;
import org.harper.otms.auth.service.AuthService;
import org.harper.otms.auth.service.ErrorCode;
import org.harper.otms.auth.service.TokenManager;
import org.harper.otms.auth.service.dto.LoginRequestDto;
import org.harper.otms.auth.service.dto.LoginResponseDto;
import org.harper.otms.auth.service.util.PasswordUtil;
import org.springframework.util.StringUtils;

public class DefaultAuthService implements AuthService {

	@Override
	public LoginResponseDto login(LoginRequestDto request) {
		User user = null;
		if (request.isLinkLogin()) {
			ExternalSystem system = null;
			try {
				system = ExternalSystem.valueOf(request.getSourceSystem());
			} catch (Exception e) {
				return new LoginResponseDto(ErrorCode.USER_UNKNOWN_SOURCE);
			}
			String id = TokenProcessor.getInstance(system).process(
					request.getSourceId());
			if (StringUtils.isEmpty(id)) {
				return new LoginResponseDto(ErrorCode.USER_FAIL_LOGIN);
			}
			user = getUserDao().findBySource(system, id);
			if (null == user) {
				return new LoginResponseDto(ErrorCode.USER_NOT_LINKED);
			}
		} else {
			user = getUserDao().findByName(request.getUsername());
			if (user == null) {
				user = getUserDao().findByEmail(request.getUsername());
			}
			if (user == null) {
				return new LoginResponseDto(ErrorCode.USER_FAIL_LOGIN);
			}
			String digest = PasswordUtil.digest(request.getPassword());
			if (!user.getPassword().equals(digest)) {
				return new LoginResponseDto(ErrorCode.USER_FAIL_LOGIN);
			}
		}
		if (!user.isActivated()) {
			return new LoginResponseDto(ErrorCode.USER_NOT_ACTIVATED);
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

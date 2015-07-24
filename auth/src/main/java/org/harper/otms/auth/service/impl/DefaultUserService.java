package org.harper.otms.auth.service.impl;

import java.util.TimeZone;

import org.harper.otms.auth.dao.UserDao;
import org.harper.otms.auth.entity.User;
import org.harper.otms.auth.service.ErrorCode;
import org.harper.otms.auth.service.UserService;
import org.harper.otms.auth.service.dto.CreateUserDto;
import org.harper.otms.auth.service.dto.CreateUserResponseDto;
import org.harper.otms.auth.service.util.PasswordUtil;

public class DefaultUserService implements UserService {

	@Override
	public CreateUserResponseDto createUser(CreateUserDto request) {
		CreateUserResponseDto result = new CreateUserResponseDto();

		// Create User
		User user = new User();

		if (getUserDao().findByName(request.getUsername()) != null) {
			result.setErrorCode(ErrorCode.USER_EXIST_ID);
			return result;
		}
		if (getUserDao().findByEmail(request.getEmail()) != null) {
			result.setErrorCode(ErrorCode.USER_EXIST_EMAIL);
			return result;
		}

		user.setName(request.getUsername());
		user.setEmail(request.getEmail());
		user.setPassword(PasswordUtil.digest(request.getPassword()));
		user.setDisplayName(request.getDisplayName());
		user.setTimezone(TimeZone.getTimeZone(request.getTimezone()));
		getUserDao().save(user);

		result.setUserId(user.getId());

		return result;
	}

	private UserDao userDao;

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}

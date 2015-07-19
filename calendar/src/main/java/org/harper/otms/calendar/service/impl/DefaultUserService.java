package org.harper.otms.calendar.service.impl;

import java.util.TimeZone;

import org.harper.otms.auth.dao.UserDao;
import org.harper.otms.auth.entity.User;
import org.harper.otms.calendar.dao.ClientDao;
import org.harper.otms.calendar.dao.TutorDao;
import org.harper.otms.calendar.entity.Client;
import org.harper.otms.calendar.entity.Tutor;
import org.harper.otms.calendar.service.ErrorCode;
import org.harper.otms.calendar.service.UserService;
import org.harper.otms.calendar.service.dto.CreateUserDto;
import org.harper.otms.calendar.service.dto.CreateUserResponseDto;
import org.harper.otms.calendar.service.util.PasswordUtil;

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

		if ("tutor".equals(request.getType())) {
			Tutor tutor = new Tutor();
			tutor.setUser(user);
			user.setType("tutor");
			getTutorDao().save(tutor);
		} else {
			Client client = new Client();
			client.setUser(user);
			user.setType("client");
			getClientDao().save(client);
		}

		return result;
	}

	private UserDao userDao;

	private TutorDao tutorDao;

	private ClientDao clientDao;

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public TutorDao getTutorDao() {
		return tutorDao;
	}

	public void setTutorDao(TutorDao tutorDao) {
		this.tutorDao = tutorDao;
	}

	public ClientDao getClientDao() {
		return clientDao;
	}

	public void setClientDao(ClientDao clientDao) {
		this.clientDao = clientDao;
	}

}

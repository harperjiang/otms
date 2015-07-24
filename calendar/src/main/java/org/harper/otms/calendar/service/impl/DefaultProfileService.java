package org.harper.otms.calendar.service.impl;

import org.harper.otms.auth.dao.UserDao;
import org.harper.otms.auth.entity.User;
import org.harper.otms.auth.service.UserService;
import org.harper.otms.auth.service.dto.CreateUserResponseDto;
import org.harper.otms.calendar.dao.ClientDao;
import org.harper.otms.calendar.dao.TutorDao;
import org.harper.otms.calendar.entity.Client;
import org.harper.otms.calendar.entity.Tutor;
import org.harper.otms.calendar.service.ProfileService;
import org.harper.otms.calendar.service.dto.RegisterUserDto;
import org.harper.otms.calendar.service.dto.RegisterUserResponseDto;

public class DefaultProfileService implements ProfileService {

	@Override
	public RegisterUserResponseDto registerUser(RegisterUserDto request) {

		CreateUserResponseDto resp = getUserService().createUser(request);
		if (resp.isSuccess()) {
			User user = getUserDao().findById(resp.getUserId());

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
			return new RegisterUserResponseDto();
		} else {
			return new RegisterUserResponseDto(resp.getErrorCode());
		}
	}

	private UserService userService;

	private UserDao userDao;

	private TutorDao tutorDao;

	private ClientDao clientDao;

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
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

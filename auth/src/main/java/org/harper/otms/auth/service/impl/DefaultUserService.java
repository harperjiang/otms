package org.harper.otms.auth.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

import org.harper.otms.auth.dao.UserDao;
import org.harper.otms.auth.entity.User;
import org.harper.otms.auth.service.ErrorCode;
import org.harper.otms.auth.service.UserService;
import org.harper.otms.auth.service.dto.ActivateUserDto;
import org.harper.otms.auth.service.dto.ActivateUserResponseDto;
import org.harper.otms.auth.service.dto.CreateUserDto;
import org.harper.otms.auth.service.dto.CreateUserResponseDto;
import org.harper.otms.auth.service.util.PasswordUtil;
import org.harper.otms.common.mail.VelocityHtmlMessagePreparator;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.StringUtils;

public class DefaultUserService implements UserService {

	protected static Set<String> VALID_TYPE = new HashSet<String>();

	static {
		VALID_TYPE.add("tutor");
		VALID_TYPE.add("client");
	}

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
		if (!StringUtils.isEmpty(request.getPassword())) {
			user.setPassword(PasswordUtil.digest(request.getPassword()));
		}
		user.setDisplayName(request.getDisplayName());
		if (!StringUtils.isEmpty(request.getTimezone())) {
			user.setTimezone(TimeZone.getTimeZone(request.getTimezone()));
		}
		user.setActivated(false);
		if (!VALID_TYPE.contains(request.getType())) {
			return new CreateUserResponseDto(ErrorCode.SYSTEM_INVALID_PARAM);
		}
		user.setType(request.getType());
		user.setActivationCode(UUID.randomUUID().toString());
		getUserDao().save(user);

		result.setUserId(user.getId());
		if (!request.isLinkUser()) {
			// Send out registration email

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("username", user.getDisplayName());
			params.put("uuid", user.getActivationCode());
			getMailSender()
					.send(new VelocityHtmlMessagePreparator(
							"harper@tutorcan.com",
							new String[] { user.getEmail() },
							"Activate your TutorCan account",
							"/org/harper/otms/auth/service/impl/mail/signup_mail.vm",
							params));
		}
		return result;
	}

	@Override
	public ActivateUserResponseDto activateUser(ActivateUserDto request) {
		User user = getUserDao().findByActivateCode(request.getUuid());
		if (user != null) {
			user.setActivated(true);
			user.setActivationCode(null);
			return new ActivateUserResponseDto();
		} else {
			return new ActivateUserResponseDto(ErrorCode.USER_ALREADY_ACTIVATED);
		}
	}

	private UserDao userDao;

	private JavaMailSender mailSender;

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public JavaMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

}

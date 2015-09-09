package org.harper.otms.auth.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.harper.otms.auth.dao.UserDao;
import org.harper.otms.auth.entity.User;
import org.harper.otms.auth.external.ExternalSystem;
import org.harper.otms.auth.external.TokenProcessor;
import org.harper.otms.auth.service.AuthService;
import org.harper.otms.auth.service.ErrorCode;
import org.harper.otms.auth.service.TokenManager;
import org.harper.otms.auth.service.dto.ConfirmResetPassDto;
import org.harper.otms.auth.service.dto.ConfirmResetPassResponseDto;
import org.harper.otms.auth.service.dto.LoginRequestDto;
import org.harper.otms.auth.service.dto.LoginResponseDto;
import org.harper.otms.auth.service.dto.ReqResetPassDto;
import org.harper.otms.auth.service.dto.ReqResetPassResponseDto;
import org.harper.otms.auth.service.util.CaptchaUtil;
import org.harper.otms.auth.service.util.PasswordUtil;
import org.harper.otms.common.mail.VelocityHtmlMessagePreparator;
import org.harper.otms.common.servlet.IPMonitorFilter;
import org.springframework.mail.javamail.JavaMailSender;
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
			// Verify Captcha
			if (!CaptchaUtil.verify(request.getCaptcha(),
					IPMonitorFilter.ipAddress.get())) {
				return new LoginResponseDto(ErrorCode.SYSTEM_CAPTCHA_FAIL);
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

	@Override
	public ReqResetPassResponseDto reqResetPass(ReqResetPassDto request) {
		if (!CaptchaUtil.verify(request.getCaptcha(),
				IPMonitorFilter.ipAddress.get())) {
			return new ReqResetPassResponseDto(ErrorCode.SYSTEM_CAPTCHA_FAIL);
		}
		User user = getUserDao().findByName(request.getUsername());
		if (user == null) {
			user = getUserDao().findByEmail(request.getUsername());
		}
		if (user == null) {
			return new ReqResetPassResponseDto(ErrorCode.SYSTEM_DATA_NOT_FOUND);
		}
		if (user.getSourceSystem() != null) {
			// ExternalSystem user should not use forget password
			return new ReqResetPassResponseDto(ErrorCode.SYSTEM_DATA_NOT_FOUND);
		}
		String uuid = UUID.randomUUID().toString();
		// Send reset link
		user.setActivationCode(uuid);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uuid", user.getActivationCode());
		getMailSender()
				.send(new VelocityHtmlMessagePreparator(
						"harper@tutorcan.com",
						new String[] { user.getEmail() },
						"Activate your TutorCan account",
						"/org/harper/otms/auth/service/impl/mail/resetpass_mail.vm",
						params));

		return new ReqResetPassResponseDto();
	}

	@Override
	public ConfirmResetPassResponseDto confirmResetPass(
			ConfirmResetPassDto request) {
		User user = getUserDao().findByActivateCode(request.getUuid());
		if (null == user) {
			return new ConfirmResetPassResponseDto(
					ErrorCode.SYSTEM_DATA_NOT_FOUND);
		}
		user.setActivationCode(null);
		user.setPassword(request.getNewpass());
		return new ConfirmResetPassResponseDto();
	}

	private UserDao userDao;

	private TokenManager tokenManager;

	private JavaMailSender mailSender;

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

	public JavaMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

}

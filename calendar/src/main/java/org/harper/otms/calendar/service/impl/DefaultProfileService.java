package org.harper.otms.calendar.service.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.TimeZone;
import java.util.UUID;

import org.eclipse.persistence.tools.file.FileUtil;
import org.harper.otms.auth.dao.UserDao;
import org.harper.otms.auth.entity.User;
import org.harper.otms.auth.external.ExternalSystem;
import org.harper.otms.auth.external.TokenProcessor;
import org.harper.otms.auth.service.UserService;
import org.harper.otms.auth.service.dto.CreateUserDto;
import org.harper.otms.auth.service.dto.CreateUserResponseDto;
import org.harper.otms.auth.service.util.CaptchaUtil;
import org.harper.otms.calendar.dao.ClientDao;
import org.harper.otms.calendar.dao.TutorDao;
import org.harper.otms.calendar.entity.profile.Client;
import org.harper.otms.calendar.entity.profile.Tutor;
import org.harper.otms.calendar.service.ErrorCode;
import org.harper.otms.calendar.service.ProfileService;
import org.harper.otms.calendar.service.dto.ClientInfoDto;
import org.harper.otms.calendar.service.dto.GetClientInfoDto;
import org.harper.otms.calendar.service.dto.GetClientInfoResponseDto;
import org.harper.otms.calendar.service.dto.LinkAddInfoDto;
import org.harper.otms.calendar.service.dto.LinkAddInfoResponseDto;
import org.harper.otms.calendar.service.dto.LinkUserDto;
import org.harper.otms.calendar.service.dto.LinkUserResponseDto;
import org.harper.otms.calendar.service.dto.RegisterUserDto;
import org.harper.otms.calendar.service.dto.RegisterUserResponseDto;
import org.harper.otms.calendar.service.dto.SetupClientDto;
import org.harper.otms.calendar.service.dto.SetupClientResponseDto;
import org.harper.otms.calendar.service.dto.UploadProfileImageDto;
import org.harper.otms.calendar.service.dto.UploadProfileImageResponseDto;
import org.harper.otms.common.servlet.IPMonitorFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class DefaultProfileService implements ProfileService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public RegisterUserResponseDto registerUser(RegisterUserDto request) {

		if (!CaptchaUtil.verify(request.getCaptcha(), IPMonitorFilter.ipAddress.get())) {
			return new RegisterUserResponseDto(ErrorCode.SYSTEM_CAPTCHA_FAIL);
		}
		request.setVerifyEmail(true);
		CreateUserResponseDto resp = getUserService().createUser(request);
		if (resp.isSuccess()) {
			User user = getUserDao().findById(resp.getUserId());

			if ("tutor".equals(request.getType())) {
				Tutor tutor = new Tutor();
				tutor.setUser(user);
				getTutorDao().save(tutor);
			} else {
				Client client = new Client();
				client.setUser(user);
				getClientDao().save(client);
			}
			return new RegisterUserResponseDto();
		} else {
			return new RegisterUserResponseDto(resp.getErrorCode());
		}
	}

	@Override
	public LinkUserResponseDto linkUser(LinkUserDto request) {

		ExternalSystem sourceSystem = null;

		try {
			sourceSystem = ExternalSystem.valueOf(request.getSourceSystem());
		} catch (Exception e) {
			return new LinkUserResponseDto(ErrorCode.USER_UNKNOWN_SOURCE);
		}
		// Verify User information first
		String sourceId = TokenProcessor.getInstance(sourceSystem).process(request.getSourceId());
		if (StringUtils.isEmpty(sourceId)) {
			return new LinkUserResponseDto(ErrorCode.USER_LINK_FAILED);
		}

		CreateUserDto create = new CreateUserDto();
		create.setDisplayName(request.getDisplayName());
		create.setEmail(request.getEmail());
		if (StringUtils.isEmpty(request.getUsername())) {
			create.setUsername(MessageFormat.format("{0}-{1}", request.getSourceSystem(), sourceId));
		} else {
			create.setUsername(request.getUsername());
		}

		create.setType(request.getType());
		create.setTimezone(request.getTimezone());
		create.setVerifyEmail(request.isVerifyEmail());
		create.setLinkUser(true);
		CreateUserResponseDto resp = getUserService().createUser(create);

		if (resp.isSuccess()) {
			User user = getUserDao().findById(resp.getUserId());
			user.setSourceSystem(sourceSystem);
			user.setSourceId(sourceId);
			if (StringUtils.isEmpty(request.getUsername()))
				user.setActivationCode("modify_username");
			if ("tutor".equals(request.getType())) {
				Tutor tutor = new Tutor();
				tutor.setUser(user);
				tutor.setPictureUrl(request.getPictureUrl());
				getTutorDao().save(tutor);
			} else {
				Client client = new Client();
				client.setUser(user);
				client.setPictureUrl(request.getPictureUrl());
				getClientDao().save(client);
			}
			return new LinkUserResponseDto(request.isVerifyEmail());
		} else {
			return new LinkUserResponseDto(resp.getErrorCode());
		}
	}

	@Override
	public LinkAddInfoResponseDto linkAddInfo(LinkAddInfoDto request) {
		User user = getUserDao().findById(request.getCurrentUser());
		if (!"modify_username".equals(user.getActivationCode())) {
			return new LinkAddInfoResponseDto(ErrorCode.SYSTEM_NO_AUTH);
		}
		User dup = getUserDao().findByName(request.getUsername());
		if (null != dup) {
			return new LinkAddInfoResponseDto(ErrorCode.USER_EXIST_ID);
		}
		user.setName(request.getUsername());
		user.setActivationCode(null);
		user.setTimezone(TimeZone.getTimeZone(request.getTimezone()));
		LinkAddInfoResponseDto response = new LinkAddInfoResponseDto();
		response.setUsername(user.getName());
		return response;
	}

	@Override
	public GetClientInfoResponseDto getClientInfo(GetClientInfoDto request) {
		Client client = getClientDao().findById(request.getClientId());

		ClientInfoDto infoDto = new ClientInfoDto();
		infoDto.from(client);

		GetClientInfoResponseDto result = new GetClientInfoResponseDto();
		result.setClientInfo(infoDto);

		return result;
	}

	@Override
	public UploadProfileImageResponseDto uploadProfileImage(UploadProfileImageDto request) {
		String uuid = UUID.randomUUID().toString();
		String fileName = MessageFormat.format("{0}/{1}.jpg", getImageFolder(), uuid);
		try {
			FileUtil.copy(request.getImage(), new FileOutputStream(fileName));
		} catch (IOException e) {
			logger.error("Error while saving file to disk", e);
			throw new RuntimeException(e);
		}
		String url = MessageFormat.format("profileImage/{0}.jpg", uuid);

		UploadProfileImageResponseDto result = new UploadProfileImageResponseDto();
		result.setImageUrl(url);
		return result;
	}

	@Override
	public SetupClientResponseDto setupClient(SetupClientDto request) {
		if (request.getCurrentUser() != request.getClientInfo().getClientId()) {
			return new SetupClientResponseDto(ErrorCode.SYSTEM_NO_AUTH);
		}
		Client client = getClientDao().findById(request.getCurrentUser());
		request.getClientInfo().to(client);
		return new SetupClientResponseDto();
	}

	private UserService userService;

	private UserDao userDao;

	private TutorDao tutorDao;

	private ClientDao clientDao;

	private String imageFolder;

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

	public String getImageFolder() {
		return imageFolder;
	}

	public void setImageFolder(String imageFolder) {
		this.imageFolder = imageFolder;
	}

}

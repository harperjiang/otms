package org.harper.otms.calendar.service.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.UUID;

import org.eclipse.persistence.tools.file.FileUtil;
import org.harper.otms.auth.dao.UserDao;
import org.harper.otms.auth.entity.User;
import org.harper.otms.auth.service.UserService;
import org.harper.otms.auth.service.dto.CreateUserResponseDto;
import org.harper.otms.calendar.dao.ClientDao;
import org.harper.otms.calendar.dao.TutorDao;
import org.harper.otms.calendar.entity.Client;
import org.harper.otms.calendar.entity.Tutor;
import org.harper.otms.calendar.service.ErrorCode;
import org.harper.otms.calendar.service.ProfileService;
import org.harper.otms.calendar.service.UploadProfileImageDto;
import org.harper.otms.calendar.service.UploadProfileImageResponseDto;
import org.harper.otms.calendar.service.dto.ClientInfoDto;
import org.harper.otms.calendar.service.dto.GetClientInfoDto;
import org.harper.otms.calendar.service.dto.GetClientInfoResponseDto;
import org.harper.otms.calendar.service.dto.GetTutorInfoDto;
import org.harper.otms.calendar.service.dto.GetTutorInfoResponseDto;
import org.harper.otms.calendar.service.dto.RegisterUserDto;
import org.harper.otms.calendar.service.dto.RegisterUserResponseDto;
import org.harper.otms.calendar.service.dto.SetupClientDto;
import org.harper.otms.calendar.service.dto.SetupClientResponseDto;
import org.harper.otms.calendar.service.dto.SetupTutorDto;
import org.harper.otms.calendar.service.dto.SetupTutorResponseDto;
import org.harper.otms.calendar.service.dto.TutorInfoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultProfileService implements ProfileService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public RegisterUserResponseDto registerUser(RegisterUserDto request) {

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
	public GetClientInfoResponseDto getClientInfo(GetClientInfoDto request) {
		Client client = getClientDao().findById(request.getClientId());

		ClientInfoDto infoDto = new ClientInfoDto();
		infoDto.from(client);

		GetClientInfoResponseDto result = new GetClientInfoResponseDto();
		result.setClientInfo(infoDto);

		return result;
	}

	@Override
	public GetTutorInfoResponseDto getTutorInfo(GetTutorInfoDto request) {
		Tutor tutor = getTutorDao().findById(request.getTutorId());

		TutorInfoDto infoDto = new TutorInfoDto();
		infoDto.from(tutor);

		GetTutorInfoResponseDto result = new GetTutorInfoResponseDto();
		result.setTutorInfo(infoDto);
		return result;
	}

	@Override
	public UploadProfileImageResponseDto uploadProfileImage(
			UploadProfileImageDto request) {
		String uuid = UUID.randomUUID().toString();
		String fileName = MessageFormat.format("{0}/{1}.jpg", getImageFolder(),
				uuid);
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
			return new SetupClientResponseDto(ErrorCode.SYS_NO_AUTH);
		}
		Client client = getClientDao().findById(request.getCurrentUser());
		request.getClientInfo().to(client);
		return new SetupClientResponseDto();
	}

	@Override
	public SetupTutorResponseDto setupTutor(SetupTutorDto request) {
		if (request.getCurrentUser() != request.getTutorInfo().getTutorId()) {
			return new SetupTutorResponseDto(ErrorCode.SYS_NO_AUTH);
		}
		Tutor tutor = getTutorDao().findById(request.getCurrentUser());
		request.getTutorInfo().to(tutor);
		return new SetupTutorResponseDto();
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

package org.harper.otms.calendar.service;

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

public interface ProfileService {

	/**
	 * 
	 * @param request
	 * @return
	 */
	public RegisterUserResponseDto registerUser(RegisterUserDto request);

	/**
	 * Link an user from other system to our system
	 * 
	 * @param request
	 * @return
	 */
	LinkUserResponseDto linkUser(LinkUserDto request);

	/**
	 * 
	 * @param request
	 * @return
	 */
	LinkAddInfoResponseDto linkAddInfo(LinkAddInfoDto request);

	/**
	 * 
	 * @param request
	 * @return
	 */
	public GetClientInfoResponseDto getClientInfo(GetClientInfoDto request);

	/**
	 * 
	 * @param request
	 * @return
	 */
	public SetupClientResponseDto setupClient(SetupClientDto request);

	/**
	 * 
	 * @param request
	 * @return
	 */
	public UploadProfileImageResponseDto uploadProfileImage(UploadProfileImageDto request);
}

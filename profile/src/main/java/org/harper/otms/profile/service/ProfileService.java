package org.harper.otms.profile.service;

import org.harper.otms.profile.service.dto.GetClientInfoDto;
import org.harper.otms.profile.service.dto.GetClientInfoResponseDto;
import org.harper.otms.profile.service.dto.LinkAddInfoDto;
import org.harper.otms.profile.service.dto.LinkAddInfoResponseDto;
import org.harper.otms.profile.service.dto.LinkUserDto;
import org.harper.otms.profile.service.dto.LinkUserResponseDto;
import org.harper.otms.profile.service.dto.RegisterUserDto;
import org.harper.otms.profile.service.dto.RegisterUserResponseDto;
import org.harper.otms.profile.service.dto.SetupClientDto;
import org.harper.otms.profile.service.dto.SetupClientResponseDto;
import org.harper.otms.profile.service.dto.UploadProfileImageDto;
import org.harper.otms.profile.service.dto.UploadProfileImageResponseDto;

public interface ProfileService {

	/**
	 * 
	 * @param request
	 * @return
	 */
	RegisterUserResponseDto registerUser(RegisterUserDto request);

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

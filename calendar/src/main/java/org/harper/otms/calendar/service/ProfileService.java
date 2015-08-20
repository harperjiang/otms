package org.harper.otms.calendar.service;

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

public interface ProfileService {

	public RegisterUserResponseDto registerUser(RegisterUserDto request);

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
	public GetTutorInfoResponseDto getTutorInfo(GetTutorInfoDto request);

	/**
	 * 
	 * @param request
	 * @return
	 */
	public SetupTutorResponseDto setupTutor(SetupTutorDto request);

	/**
	 * 
	 * @param request
	 * @return
	 */
	public UploadProfileImageResponseDto uploadProfileImage(
			UploadProfileImageDto request);
}

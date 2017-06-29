package org.harper.otms.profile.service;

import org.harper.otms.profile.service.dto.FindTutorDto;
import org.harper.otms.profile.service.dto.FindTutorResponseDto;
import org.harper.otms.profile.service.dto.GetPopularTutorDto;
import org.harper.otms.profile.service.dto.GetPopularTutorResponseDto;
import org.harper.otms.profile.service.dto.GetTimesheetDto;
import org.harper.otms.profile.service.dto.GetTimesheetResponseDto;
import org.harper.otms.profile.service.dto.GetTutorInfoDto;
import org.harper.otms.profile.service.dto.GetTutorInfoResponseDto;
import org.harper.otms.profile.service.dto.SetupTimesheetDto;
import org.harper.otms.profile.service.dto.SetupTimesheetResponseDto;
import org.harper.otms.profile.service.dto.SetupTutorDto;
import org.harper.otms.profile.service.dto.SetupTutorResponseDto;

public interface TutorService {

	/**
	 * 
	 * @param request
	 * @return
	 */
	GetTimesheetResponseDto getTimesheet(GetTimesheetDto request);

	/**
	 * 
	 * @param request
	 * @return
	 */
	SetupTimesheetResponseDto setupTimesheet(SetupTimesheetDto request);

	/**
	 * 
	 * @param request
	 * @return
	 */
	GetPopularTutorResponseDto getPopularTutors(GetPopularTutorDto request);

	/**
	 * 
	 * @param request
	 * @return
	 */
	FindTutorResponseDto findTutors(FindTutorDto request);

	/**
	 * 
	 * @param request
	 * @return
	 */
	GetTutorInfoResponseDto getTutorInfo(GetTutorInfoDto request);

	/**
	 * 
	 * @param request
	 * @return
	 */
	SetupTutorResponseDto setupTutor(SetupTutorDto request);

}

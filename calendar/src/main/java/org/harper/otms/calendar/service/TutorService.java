package org.harper.otms.calendar.service;

import org.harper.otms.calendar.service.dto.FindTutorDto;
import org.harper.otms.calendar.service.dto.FindTutorResponseDto;
import org.harper.otms.calendar.service.dto.GetPopularTutorDto;
import org.harper.otms.calendar.service.dto.GetPopularTutorResponseDto;
import org.harper.otms.calendar.service.dto.GetTimesheetDto;
import org.harper.otms.calendar.service.dto.GetTimesheetResponseDto;
import org.harper.otms.calendar.service.dto.GetTutorInfoDto;
import org.harper.otms.calendar.service.dto.GetTutorInfoResponseDto;
import org.harper.otms.calendar.service.dto.SetupTimesheetDto;
import org.harper.otms.calendar.service.dto.SetupTimesheetResponseDto;
import org.harper.otms.calendar.service.dto.SetupTutorDto;
import org.harper.otms.calendar.service.dto.SetupTutorResponseDto;

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

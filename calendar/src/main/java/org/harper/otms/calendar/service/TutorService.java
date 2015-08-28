package org.harper.otms.calendar.service;

import org.harper.otms.calendar.service.dto.FindTutorDto;
import org.harper.otms.calendar.service.dto.FindTutorResponseDto;
import org.harper.otms.calendar.service.dto.GetPopularTutorDto;
import org.harper.otms.calendar.service.dto.GetPopularTutorResponseDto;
import org.harper.otms.calendar.service.dto.GetTimesheetDto;
import org.harper.otms.calendar.service.dto.GetTimesheetResponseDto;
import org.harper.otms.calendar.service.dto.SetupTimesheetDto;
import org.harper.otms.calendar.service.dto.SetupTimesheetResponseDto;


public interface TutorService {
	
	GetTimesheetResponseDto getTimesheet(GetTimesheetDto request);
	
	SetupTimesheetResponseDto setupTimesheet(SetupTimesheetDto request);
	
	GetPopularTutorResponseDto getPopularTutors(GetPopularTutorDto request);
	
	FindTutorResponseDto findTutors(FindTutorDto request);
}

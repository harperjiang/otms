package org.harper.otms.calendar.service;

import org.harper.otms.calendar.service.dto.GetTimesheetDto;
import org.harper.otms.calendar.service.dto.GetTimesheetResponseDto;
import org.harper.otms.calendar.service.dto.SetupTimesheetDto;
import org.harper.otms.calendar.service.dto.SetupTimesheetResponseDto;


public interface TutorService {
	
	GetTimesheetResponseDto getTimesheet(GetTimesheetDto request);
	
	SetupTimesheetResponseDto setupTimesheet(SetupTimesheetDto request);
	
}

package org.harper.otms.lesson.service;

import org.harper.otms.lesson.service.dto.GetCalendarEventDto;
import org.harper.otms.lesson.service.dto.GetCalendarEventResponseDto;
import org.harper.otms.lesson.service.dto.ViewTimesheetDto;
import org.harper.otms.lesson.service.dto.ViewTimesheetResponseDto;

public interface CalendarService {

	/**
	 * Retrieve calendar events associated with a specific date range
	 * 
	 * @param request
	 * @return
	 */
	GetCalendarEventResponseDto getEvents(GetCalendarEventDto request);

	ViewTimesheetResponseDto viewTimesheet(ViewTimesheetDto request);
}

package org.harper.otms.calendar.service;

import org.harper.otms.calendar.service.dto.GetCalendarEventDto;
import org.harper.otms.calendar.service.dto.GetCalendarEventResponseDto;

public interface CalendarService {

	/**
	 * Retrieve calendar events associated with a specific date range
	 * 
	 * @param request
	 * @return
	 */
	GetCalendarEventResponseDto getEvents(GetCalendarEventDto request);

}

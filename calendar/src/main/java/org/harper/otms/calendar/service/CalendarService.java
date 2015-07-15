package org.harper.otms.calendar.service;

import org.harper.otms.calendar.service.dto.CancelLessonDto;
import org.harper.otms.calendar.service.dto.ConfirmCancelDto;
import org.harper.otms.calendar.service.dto.GetCalendarEventDto;
import org.harper.otms.calendar.service.dto.GetCalendarEventResponseDto;
import org.harper.otms.calendar.service.dto.SetupLessonDto;

public interface CalendarService {

	/**
	 * Retrieve calendar events associated with a specific date range
	 * @param request
	 * @return
	 */
	GetCalendarEventResponseDto getCalendarEvents(GetCalendarEventDto request);
	
	/**
	 * Client setup meeting with tutor
	 * @param request
	 */
	void setupLesson(SetupLessonDto request);

	/**
	 * Both client and tutor can propose the cancellation of a meeting. 
	 * Client can directly cancel a meeting
	 * Tutor always need to get approval from client to cancel a meeting
	 * @param request
	 */
	void cancelLesson(CancelLessonDto request);
	
	/**
	 * Client confirm tutor's request of canceling a meeting or a item
	 * @param request
	 */
	void confirmCancel(ConfirmCancelDto request);
	
	
	/**
	 * Clear finished calendar item and mark it as snapshots
	 */
	void updateSnapshot();
}

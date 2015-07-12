package org.harper.otms.calendar.service;

import org.harper.otms.calendar.service.dto.CancelMeetingDto;
import org.harper.otms.calendar.service.dto.CancelMeetingItemDto;
import org.harper.otms.calendar.service.dto.ConfirmCancelDto;
import org.harper.otms.calendar.service.dto.SetupOneOffMeetingDto;
import org.harper.otms.calendar.service.dto.SetupRepeatedMeetingDto;

public interface CalendarService {

	/**
	 * Client setup meeting with tutor
	 * @param request
	 */
	void setupOneOffMeeting(SetupOneOffMeetingDto request);

	/**
	 * Client setup meeting with tutor
	 * @param request
	 */
	void setupRepeatedMeeting(SetupRepeatedMeetingDto request);

	/**
	 * Both client and tutor can propose the cancellation of a meeting. 
	 * Client can directly cancel a meeting
	 * Tutor always need to get approval from client to cancel a meeting
	 * @param request
	 */
	void cancelMeeting(CancelMeetingDto request);
	
	/**
	 * Both client and tutor can cancel a single item of meeting.
	 * The same rule as {@link #cancelMeeting(CancelMeetingDto)} applies here.
	 * @see #cancelMeeting(CancelMeetingDto)
	 * @param request
	 */
	void cancelMeetingItem(CancelMeetingItemDto request);

	/**
	 * Client confirm tutor's request of canceling a meeting or a item
	 * @param request
	 */
	void confirmCancel(ConfirmCancelDto request);
}

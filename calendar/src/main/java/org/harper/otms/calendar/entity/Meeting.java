package org.harper.otms.calendar.entity;

public class Meeting {

	public static enum Status {
		REQUESTED, REJECTED, VALID, CANCELING, CANCELED
	}

	private Tutor tutor;

	private Client client;

	private Status status;

	/**
	 * Whether this event will auto skip tutor's holiday
	 */
	private boolean skipHoliday;

	private CalendarEntry calendar;
}

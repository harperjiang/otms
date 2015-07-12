package org.harper.otms.calendar.entity;

import java.util.Date;

public class CalendarItem {

	public static enum Status {
		VALID, CANCELING, CANCELED
	}

	private CalendarEntry entry;

	private Date date;

	private int startTime;

	private int stopTime;
}

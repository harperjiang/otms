package org.harper.otms.calendar.entity;

import java.util.Date;

public class RepeatedEntry extends CalendarEntry {

	public static enum Interval {
		DAY, WEEK, BIWEEK, MONTH
	}

	private Date startDate;

	private Date stopDate;

	private Interval interval;

	private String dateExpression;

	private int startTime;

	private int stopTime;
}

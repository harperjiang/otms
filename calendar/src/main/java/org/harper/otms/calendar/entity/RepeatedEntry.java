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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getStopDate() {
		return stopDate;
	}

	public void setStopDate(Date stopDate) {
		this.stopDate = stopDate;
	}

	public Interval getInterval() {
		return interval;
	}

	public void setInterval(Interval interval) {
		this.interval = interval;
	}

	public String getDateExpression() {
		return dateExpression;
	}

	public void setDateExpression(String dateExpression) {
		this.dateExpression = dateExpression;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getStopTime() {
		return stopTime;
	}

	public void setStopTime(int stopTime) {
		this.stopTime = stopTime;
	}

}

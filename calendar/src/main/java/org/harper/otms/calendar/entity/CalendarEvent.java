package org.harper.otms.calendar.entity;

import java.util.Date;

import org.harper.otms.common.dao.Entity;

public class CalendarEvent extends Entity {

	public static enum Status {
		VALID, CANCELING, CANCELED
	}

	private CalendarEntry entry;

	private Date date;

	private int startTime;

	private int stopTime;

	public CalendarEntry getEntry() {
		return entry;
	}

	public void setEntry(CalendarEntry entry) {
		this.entry = entry;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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

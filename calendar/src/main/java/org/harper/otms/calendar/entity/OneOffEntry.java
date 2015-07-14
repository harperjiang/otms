package org.harper.otms.calendar.entity;

import java.util.Date;

public class OneOffEntry extends CalendarEntry {

	private Date date;

	private int startTime;

	private int stopTime;

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

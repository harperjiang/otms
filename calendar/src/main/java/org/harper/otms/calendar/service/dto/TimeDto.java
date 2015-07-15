package org.harper.otms.calendar.service.dto;

public class TimeDto {

	private int hour;

	private int minute;

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int total() {
		return hour * 60 + minute;
	}

}

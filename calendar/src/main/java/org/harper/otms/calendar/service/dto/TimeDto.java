package org.harper.otms.calendar.service.dto;

public class TimeDto {

	private int hour;

	private int minute;

	public TimeDto() {
		
	}
	
	public TimeDto(int val) {
		this();
		set(val);
	}
	
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

	public void set(int val) {
		this.hour = val / 60;
		this.minute = val % 60;
	}

}

package org.harper.otms.calendar.entity;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.harper.otms.auth.entity.User;

public class Tutor extends User {

	private TimeZone timezone;

	private Timesheet timesheet = new Timesheet();

	private List<Date> holidays;

	public TimeZone getTimezone() {
		return timezone;
	}

	public void setTimezone(TimeZone timezone) {
		this.timezone = timezone;
	}

	public Timesheet getTimesheet() {
		return timesheet;
	}

	public void setTimesheet(Timesheet timesheet) {
		this.timesheet = timesheet;
	}

	public List<Date> getHolidays() {
		return holidays;
	}

	public void setHolidays(List<Date> holidays) {
		this.holidays = holidays;
	}

}

package org.harper.otms.calendar.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.harper.otms.auth.entity.User;

@Entity
@Table(name="tutor")
public class Tutor extends User {

	@Embedded
	private Timesheet timesheet = new Timesheet();
	
	@Transient
	private List<Date> holidays;

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

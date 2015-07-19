package org.harper.otms.calendar.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.harper.otms.auth.entity.User;
import org.harper.otms.common.dao.Entity;

@javax.persistence.Entity
@Table(name = "tutor")
public class Tutor extends Entity {

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Embedded
	private Timesheet timesheet = new Timesheet();

	@Transient
	private List<Date> holidays;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		this.setId(user.getId());
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

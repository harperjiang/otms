package org.harper.otms.calendar.entity;

import java.util.List;
import java.util.TimeZone;

import org.harper.otms.auth.entity.User;

public class Tutor extends User {

	private TimeZone timezone;
	
	private Timesheet timesheet;
	
	private List<Meeting> holidays;
}

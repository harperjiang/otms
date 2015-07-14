package org.harper.otms.calendar.entity;

import java.util.TimeZone;

import org.harper.otms.auth.entity.User;

public class Client extends User {

	private TimeZone timeZone;

	public TimeZone getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

}

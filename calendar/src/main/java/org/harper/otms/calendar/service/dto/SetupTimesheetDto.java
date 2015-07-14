package org.harper.otms.calendar.service.dto;

import java.util.Date;

import org.harper.otms.common.dto.RequestDto;

public class SetupTimesheetDto extends RequestDto {

	private int tutorId;

	private String[] expressions;

	private Date[] holidays;

	public int getTutorId() {
		return tutorId;
	}

	public void setTutorId(int tutorId) {
		this.tutorId = tutorId;
	}

	public String[] getExpressions() {
		return expressions;
	}

	public void setExpressions(String[] expressions) {
		this.expressions = expressions;
	}

	public Date[] getHolidays() {
		return holidays;
	}

	public void setHolidays(Date[] holidays) {
		this.holidays = holidays;
	}

}

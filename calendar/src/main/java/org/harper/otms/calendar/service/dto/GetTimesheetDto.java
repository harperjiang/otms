package org.harper.otms.calendar.service.dto;

import java.util.Date;

import org.harper.otms.common.dto.RequestDto;

public class GetTimesheetDto extends RequestDto {

	private int tutorId;

	private Date weekStart;

	public int getTutorId() {
		return tutorId;
	}

	public void setTutorId(int tutorId) {
		this.tutorId = tutorId;
	}

	public Date getWeekStart() {
		return weekStart;
	}

	public void setWeekStart(Date weekStart) {
		this.weekStart = weekStart;
	}

}

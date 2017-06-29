package org.harper.otms.lesson.service.dto;

import java.util.Date;

import org.harper.otms.common.dto.RequestDto;

public class AdminSearchLessonDto extends RequestDto {

	private String tutorName;

	private String clientName;

	private Date fromDate;

	private Date toDate;

	public String getTutorName() {
		return tutorName;
	}

	public void setTutorName(String tutorName) {
		this.tutorName = tutorName;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

}

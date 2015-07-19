package org.harper.otms.calendar.service.dto;

import java.util.Date;

import org.harper.otms.common.dto.RequestDto;

public class GetCalendarEventDto extends RequestDto {

	private Date fromDate;

	private Date toDate;

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

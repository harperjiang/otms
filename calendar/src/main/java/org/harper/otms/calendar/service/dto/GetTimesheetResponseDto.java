package org.harper.otms.calendar.service.dto;

import java.util.Date;

import org.harper.otms.common.dto.ResponseDto;

public class GetTimesheetResponseDto extends ResponseDto {

	private String[] expressions;

	private Date[] holidays;

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

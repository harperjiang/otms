package org.harper.otms.calendar.service.dto;

import org.harper.otms.common.dto.RequestDto;

public class SetupTimesheetDto extends RequestDto {

	private TimesheetDto timesheet;

	public TimesheetDto getTimesheet() {
		return timesheet;
	}

	public void setTimesheet(TimesheetDto timesheet) {
		this.timesheet = timesheet;
	}

}

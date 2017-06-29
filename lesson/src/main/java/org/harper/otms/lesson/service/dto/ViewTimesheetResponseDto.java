package org.harper.otms.lesson.service.dto;

import org.harper.otms.common.dto.ResponseDto;
import org.harper.otms.profile.service.dto.TimesheetDto;

public class ViewTimesheetResponseDto extends ResponseDto {

	private TimesheetDto timesheet;

	public TimesheetDto getTimesheet() {
		return timesheet;
	}

	public void setTimesheet(TimesheetDto timesheet) {
		this.timesheet = timesheet;
	}

}

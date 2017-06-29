package org.harper.otms.profile.service.dto;

import org.harper.otms.common.dto.RequestDto;

public class GetTimesheetDto extends RequestDto {

	private int tutorId;

	public int getTutorId() {
		return tutorId;
	}

	public void setTutorId(int tutorId) {
		this.tutorId = tutorId;
	}

	@Override
	public boolean needValidation() {
		return false;
	}

}

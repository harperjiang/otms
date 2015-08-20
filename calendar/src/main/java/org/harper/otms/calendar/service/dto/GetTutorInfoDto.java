package org.harper.otms.calendar.service.dto;

import org.harper.otms.common.dto.RequestDto;

public class GetTutorInfoDto extends RequestDto {

	private int tutorId;

	public int getTutorId() {
		return tutorId;
	}

	public void setTutorId(int tutorId) {
		this.tutorId = tutorId;
	}

}

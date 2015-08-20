package org.harper.otms.calendar.service.dto;

import org.harper.otms.common.dto.RequestDto;

public class SetupTutorDto extends RequestDto {

	private TutorInfoDto tutorInfo;

	public TutorInfoDto getTutorInfo() {
		return tutorInfo;
	}

	public void setTutorInfo(TutorInfoDto tutorInfo) {
		this.tutorInfo = tutorInfo;
	}
	
	
}

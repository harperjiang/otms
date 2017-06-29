package org.harper.otms.profile.service.dto;

import org.harper.otms.common.dto.ResponseDto;

public class GetTutorInfoResponseDto extends ResponseDto {

	private TutorInfoDto tutorInfo;

	public TutorInfoDto getTutorInfo() {
		return tutorInfo;
	}

	public void setTutorInfo(TutorInfoDto tutorInfo) {
		this.tutorInfo = tutorInfo;
	}

}

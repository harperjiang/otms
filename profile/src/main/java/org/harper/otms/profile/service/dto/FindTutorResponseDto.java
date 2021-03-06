package org.harper.otms.profile.service.dto;

import java.util.List;

import org.harper.otms.common.dto.ResponseDto;

public class FindTutorResponseDto extends ResponseDto {

	private List<TutorBriefDto> tutors;

	public List<TutorBriefDto> getTutors() {
		return tutors;
	}

	public void setTutors(List<TutorBriefDto> tutors) {
		this.tutors = tutors;
	}

}

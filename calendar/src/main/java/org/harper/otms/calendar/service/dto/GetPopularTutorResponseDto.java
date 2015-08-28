package org.harper.otms.calendar.service.dto;

import java.util.List;

import org.harper.otms.common.dto.ResponseDto;

public class GetPopularTutorResponseDto extends ResponseDto {

	private List<TutorBriefDto> tutors;

	public List<TutorBriefDto> getTutors() {
		return tutors;
	}

	public void setTutors(List<TutorBriefDto> tutors) {
		this.tutors = tutors;
	}

}

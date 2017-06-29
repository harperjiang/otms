package org.harper.otms.lesson.service.dto;

import java.util.List;

import org.harper.otms.common.dto.ResponseDto;

public class GetOngoingLessonResponseDto extends ResponseDto {

	private List<LessonDto> lessons;

	public List<LessonDto> getLessons() {
		return lessons;
	}

	public void setLessons(List<LessonDto> lessons) {
		this.lessons = lessons;
	}

}

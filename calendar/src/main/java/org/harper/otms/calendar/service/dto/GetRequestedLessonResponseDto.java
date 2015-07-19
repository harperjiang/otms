package org.harper.otms.calendar.service.dto;

import org.harper.otms.common.dto.ResponseDto;

public class GetRequestedLessonResponseDto extends ResponseDto {

	private LessonDto[] lessons;

	public LessonDto[] getLessons() {
		return lessons;
	}

	public void setLessons(LessonDto[] lessons) {
		this.lessons = lessons;
	}

}

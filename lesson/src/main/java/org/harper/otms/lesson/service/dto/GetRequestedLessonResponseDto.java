package org.harper.otms.lesson.service.dto;

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

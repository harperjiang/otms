package org.harper.otms.lesson.service.dto;

import org.harper.otms.common.dto.RequestDto;

public class SetupLessonDto extends RequestDto {

	LessonDto lesson;

	public LessonDto getLesson() {
		return lesson;
	}

	public void setLesson(LessonDto lesson) {
		this.lesson = lesson;
	}

}

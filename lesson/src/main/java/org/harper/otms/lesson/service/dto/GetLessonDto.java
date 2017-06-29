package org.harper.otms.lesson.service.dto;

import org.harper.otms.common.dto.RequestDto;

public class GetLessonDto extends RequestDto {

	private int lessonId;

	public int getLessonId() {
		return lessonId;
	}

	public void setLessonId(int lessonId) {
		this.lessonId = lessonId;
	}

}

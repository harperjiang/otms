package org.harper.otms.lesson.service.dto;

import org.harper.otms.common.dto.RequestDto;

public class AdminUpdateLessonDto extends RequestDto {

	private int lessonId;

	private int lessonItemId;

	public int getLessonId() {
		return lessonId;
	}

	public void setLessonId(int lessonId) {
		this.lessonId = lessonId;
	}

	public int getLessonItemId() {
		return lessonItemId;
	}

	public void setLessonItemId(int lessonItemId) {
		this.lessonItemId = lessonItemId;
	}

}

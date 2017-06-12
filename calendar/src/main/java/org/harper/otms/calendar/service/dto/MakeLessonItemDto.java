package org.harper.otms.calendar.service.dto;

import org.harper.otms.common.dto.RequestDto;

public class MakeLessonItemDto extends RequestDto {

	int lessonId;

	int lessonItemId;

	LessonItemDto lessonItem;

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

	public LessonItemDto getLessonItem() {
		return lessonItem;
	}

	public void setLessonItem(LessonItemDto lessonItem) {
		this.lessonItem = lessonItem;
	}

}

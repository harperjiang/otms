package org.harper.otms.lesson.service.dto;

import org.harper.otms.common.dto.RequestDto;

public class GetLessonItemDto extends RequestDto {

	private int lessonItemId;

	public int getLessonItemId() {
		return lessonItemId;
	}

	public void setLessonItemId(int lessonItemId) {
		this.lessonItemId = lessonItemId;
	}

}

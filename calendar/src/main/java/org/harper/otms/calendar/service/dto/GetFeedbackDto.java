package org.harper.otms.calendar.service.dto;

import org.harper.otms.common.dto.RequestDto;

public class GetFeedbackDto extends RequestDto {

	private int lessonItemId;

	public int getLessonItemId() {
		return lessonItemId;
	}

	public void setLessonItemId(int lessonItemId) {
		this.lessonItemId = lessonItemId;
	}

}

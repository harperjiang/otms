package org.harper.otms.lesson.service.dto;

import org.harper.otms.common.dto.RequestDto;

public class ChangeLessonStatusDto extends RequestDto {

	int lessonId;

	String toStatus;

	public int getLessonId() {
		return lessonId;
	}

	public void setLessonId(int lessonId) {
		this.lessonId = lessonId;
	}

	public String getToStatus() {
		return toStatus;
	}

	public void setToStatus(String toStatus) {
		this.toStatus = toStatus;
	}

}

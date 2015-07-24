package org.harper.otms.calendar.service.dto;

import org.harper.otms.common.dto.RequestDto;

public class TriggerLessonDto extends RequestDto {

	private int lessonId;

	private int lessonItemId;

	private boolean last = false;

	@Override
	public boolean needValidation() {
		return false;
	}

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

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

}

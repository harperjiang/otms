package org.harper.otms.calendar.service.dto;

import org.harper.otms.common.dto.ResponseDto;

public class MakeLessonItemResponseDto extends ResponseDto {

	private int lessonItemId;

	public MakeLessonItemResponseDto() {
		super();
	}

	public MakeLessonItemResponseDto(int errorCode) {
		super(errorCode);
	}

	public int getLessonItemId() {
		return lessonItemId;
	}

	public void setLessonItemId(int lessonItemId) {
		this.lessonItemId = lessonItemId;
	}

}

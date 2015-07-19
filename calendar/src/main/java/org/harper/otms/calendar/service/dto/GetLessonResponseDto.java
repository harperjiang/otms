package org.harper.otms.calendar.service.dto;

import org.harper.otms.common.dto.ResponseDto;

public class GetLessonResponseDto extends ResponseDto {

	LessonDto lesson;

	private boolean owner;

	public GetLessonResponseDto() {
		super();
	}

	public GetLessonResponseDto(int err) {
		super(err);
	}

	public LessonDto getLesson() {
		return lesson;
	}

	public void setLesson(LessonDto lesson) {
		this.lesson = lesson;
	}

	public boolean isOwner() {
		return owner;
	}

	public void setOwner(boolean owner) {
		this.owner = owner;
	}

}

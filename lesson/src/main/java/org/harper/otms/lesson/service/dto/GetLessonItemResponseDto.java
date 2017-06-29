package org.harper.otms.lesson.service.dto;

import org.harper.otms.common.dto.ResponseDto;

public class GetLessonItemResponseDto extends ResponseDto {

	private boolean owner;

	public boolean isOwner() {
		return owner;
	}

	public void setOwner(boolean owner) {
		this.owner = owner;
	}

	public GetLessonItemResponseDto() {
		super();
	}

	public GetLessonItemResponseDto(int err) {
		super(err);
	}

	private LessonItemDto lessonItem;

	public LessonItemDto getLessonItem() {
		return lessonItem;
	}

	public void setLessonItem(LessonItemDto lessonItem) {
		this.lessonItem = lessonItem;
	}

}

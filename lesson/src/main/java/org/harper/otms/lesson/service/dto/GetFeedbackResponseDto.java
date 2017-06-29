package org.harper.otms.lesson.service.dto;

import org.harper.otms.common.dto.ResponseDto;

public class GetFeedbackResponseDto extends ResponseDto {

	public GetFeedbackResponseDto() {
		super();
	}

	public GetFeedbackResponseDto(int errcode) {
		super(errcode);
	}

	private LessonFeedbackDto feedback;

	private LessonItemDto lessonItem;

	public LessonFeedbackDto getFeedback() {
		return feedback;
	}

	public void setFeedback(LessonFeedbackDto feedback) {
		this.feedback = feedback;
	}

	public LessonItemDto getLessonItem() {
		return lessonItem;
	}

	public void setLessonItem(LessonItemDto lessonItem) {
		this.lessonItem = lessonItem;
	}

}

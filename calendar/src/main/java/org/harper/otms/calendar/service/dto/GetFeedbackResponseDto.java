package org.harper.otms.calendar.service.dto;

import org.harper.otms.common.dto.ResponseDto;

public class GetFeedbackResponseDto extends ResponseDto {

	public GetFeedbackResponseDto() {
		super();
	}

	public GetFeedbackResponseDto(int errcode) {
		super(errcode);
	}

	private FeedbackDto feedback;

	private LessonItemDto lessonItem;

	public FeedbackDto getFeedback() {
		return feedback;
	}

	public void setFeedback(FeedbackDto feedback) {
		this.feedback = feedback;
	}

	public LessonItemDto getLessonItem() {
		return lessonItem;
	}

	public void setLessonItem(LessonItemDto lessonItem) {
		this.lessonItem = lessonItem;
	}

}

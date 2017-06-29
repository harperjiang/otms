package org.harper.otms.lesson.service.dto;

import java.util.List;

import org.harper.otms.common.dto.ResponseDto;

public class GetTutorFeedbacksResponseDto extends ResponseDto {

	public GetTutorFeedbacksResponseDto() {
		super();
	}

	public GetTutorFeedbacksResponseDto(int errorCode) {
		super(errorCode);
	}

	private List<LessonFeedbackDto> feedbacks;

	public List<LessonFeedbackDto> getFeedbacks() {
		return feedbacks;
	}

	public void setFeedbacks(List<LessonFeedbackDto> feedbacks) {
		this.feedbacks = feedbacks;
	}

}

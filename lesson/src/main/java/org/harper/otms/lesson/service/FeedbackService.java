package org.harper.otms.lesson.service;

import org.harper.otms.lesson.service.dto.ClientFeedbackDto;
import org.harper.otms.lesson.service.dto.ClientFeedbackResponseDto;
import org.harper.otms.lesson.service.dto.GetFeedbackDto;
import org.harper.otms.lesson.service.dto.GetFeedbackResponseDto;
import org.harper.otms.lesson.service.dto.GetTutorFeedbacksDto;
import org.harper.otms.lesson.service.dto.GetTutorFeedbacksResponseDto;

public interface FeedbackService {
	/**
	 * Client leave Feedback for a lesson attended
	 * 
	 * @param request
	 * @return
	 */
	ClientFeedbackResponseDto clientFeedback(ClientFeedbackDto request);

	/**
	 * 
	 * @param request
	 * @return
	 */
	GetFeedbackResponseDto getFeedback(GetFeedbackDto request);

	/**
	 * Get all feedbacks for a given tutor
	 * 
	 * @param request
	 * @return
	 */
	GetTutorFeedbacksResponseDto getTutorFeedbacks(GetTutorFeedbacksDto request);

}

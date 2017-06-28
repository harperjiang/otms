package org.harper.otms.calendar.service;

import org.harper.otms.calendar.service.dto.ClientFeedbackDto;
import org.harper.otms.calendar.service.dto.ClientFeedbackResponseDto;
import org.harper.otms.calendar.service.dto.GetFeedbackDto;
import org.harper.otms.calendar.service.dto.GetFeedbackResponseDto;
import org.harper.otms.calendar.service.dto.GetTutorFeedbacksDto;
import org.harper.otms.calendar.service.dto.GetTutorFeedbacksResponseDto;

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

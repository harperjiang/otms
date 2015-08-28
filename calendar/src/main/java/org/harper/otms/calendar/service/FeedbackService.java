package org.harper.otms.calendar.service;

import org.harper.otms.calendar.service.dto.ClientFeedbackDto;
import org.harper.otms.calendar.service.dto.ClientFeedbackResponseDto;
import org.harper.otms.calendar.service.dto.GetFeedbackDto;
import org.harper.otms.calendar.service.dto.GetFeedbackResponseDto;
import org.harper.otms.calendar.service.dto.TutorFeedbackDto;
import org.harper.otms.calendar.service.dto.TutorFeedbackResponseDto;

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
	 * Tutor leave feedback for a lesson
	 * 
	 * @param request
	 * @return
	 */
	TutorFeedbackResponseDto tutorFeedback(TutorFeedbackDto request);

}

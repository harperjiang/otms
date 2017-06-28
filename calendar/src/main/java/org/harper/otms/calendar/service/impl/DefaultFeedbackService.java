package org.harper.otms.calendar.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.harper.otms.auth.dao.UserDao;
import org.harper.otms.auth.entity.User;
import org.harper.otms.calendar.dao.FeedbackDao;
import org.harper.otms.calendar.dao.LessonItemDao;
import org.harper.otms.calendar.dao.TodoDao;
import org.harper.otms.calendar.entity.Todo;
import org.harper.otms.calendar.entity.lesson.LessonFeedback;
import org.harper.otms.calendar.entity.lesson.LessonItem;
import org.harper.otms.calendar.entity.lesson.LessonItem.FeedbackStatus;
import org.harper.otms.calendar.service.ErrorCode;
import org.harper.otms.calendar.service.FeedbackService;
import org.harper.otms.calendar.service.dto.ClientFeedbackDto;
import org.harper.otms.calendar.service.dto.ClientFeedbackResponseDto;
import org.harper.otms.calendar.service.dto.LessonFeedbackDto;
import org.harper.otms.calendar.service.dto.GetFeedbackDto;
import org.harper.otms.calendar.service.dto.GetFeedbackResponseDto;
import org.harper.otms.calendar.service.dto.GetTutorFeedbacksDto;
import org.harper.otms.calendar.service.dto.GetTutorFeedbacksResponseDto;
import org.harper.otms.calendar.service.dto.LessonItemDto;
import org.harper.otms.calendar.service.util.DateUtil;

public class DefaultFeedbackService implements FeedbackService {

	@Override
	public ClientFeedbackResponseDto clientFeedback(ClientFeedbackDto request) {
		// Check if there is already a feedback
		if (getFeedbackDao().findByLessonItemId(request.getLessonItemId()) != null) {
			return new ClientFeedbackResponseDto(ErrorCode.LESSON_ITEM_HAS_FEEDBACK);
		}
		LessonItem item = getLessonItemDao().findById(request.getLessonItemId());
		if (item == null) {
			return new ClientFeedbackResponseDto(ErrorCode.SYSTEM_DATA_NOT_FOUND);
		}
		if (item.getLesson().getClient().getId() != request.getCurrentUser()) {
			return new ClientFeedbackResponseDto(ErrorCode.SYSTEM_NO_AUTH);
		}

		/*
		 * Save feedback item
		 */
		LessonFeedback feedback = new LessonFeedback();
		feedback.setCreateTime(DateUtil.nowUTC());
		feedback.setLessonItem(item);
		feedback.setClient(item.getLesson().getClient());
		feedback.setTutor(item.getLesson().getTutor());

		feedback.setSuccess(request.isLessonSuccess());
		if (feedback.isSuccess()) {
			feedback.setLessonRate(request.getLessonRate());
			feedback.setTutorRate(request.getTutorRate());
		} else {
			feedback.setTutorRate(request.getTutorNoattendRate());
			feedback.setFailReason(request.getNoAttendReason());
		}
		feedback.setComment(request.getComment());

		getFeedbackDao().save(feedback);

		/*
		 * Update Feedback status of lesson item
		 */
		item.setFeedbackStatus(FeedbackStatus.CLIENT_FEEDBACK);
		/*
		 * Remove related todo item
		 */
		Todo todo = getTodoDao().findByOwnerTypeAndRefId(feedback.getClient().getUser(), Todo.Type.CLIENT_FEEDBACK,
				item.getId());
		getTodoDao().delete(todo);

		return new ClientFeedbackResponseDto();
	}

	@Override
	public GetFeedbackResponseDto getFeedback(GetFeedbackDto request) {
		User currentUser = getUserDao().findById(request.getCurrentUser());
		LessonFeedback feedback = getFeedbackDao().findByLessonItemId(request.getLessonItemId());
		if (feedback == null)
			return new GetFeedbackResponseDto(ErrorCode.SYSTEM_DATA_NOT_FOUND);
		if (feedback.getTutor().getId() != request.getCurrentUser()
				&& feedback.getClient().getId() != request.getCurrentUser()) {
			return new GetFeedbackResponseDto(ErrorCode.SYSTEM_NO_AUTH);
		}

		GetFeedbackResponseDto response = new GetFeedbackResponseDto();

		LessonFeedbackDto fdto = new LessonFeedbackDto();
		fdto.from(feedback, currentUser);

		LessonItemDto ldto = new LessonItemDto();
		ldto.from(feedback.getLessonItem(), currentUser);

		response.setFeedback(fdto);
		response.setLessonItem(ldto);

		return response;
	}

	@Override
	public GetTutorFeedbacksResponseDto getTutorFeedbacks(GetTutorFeedbacksDto request) {
		User tutor = getUserDao().findById(request.getTutorId());
		if (tutor == null || !tutor.isTutor()) {
			return new GetTutorFeedbacksResponseDto(ErrorCode.SYSTEM_DATA_NOT_FOUND);
		}
		List<LessonFeedback> feedbacks = getFeedbackDao().findByTutor(request.getTutorId(), request.getLimit());
		List<LessonFeedbackDto> dtos = feedbacks.stream().map((LessonFeedback fb) -> {
			LessonFeedbackDto fbdto = new LessonFeedbackDto();
			fbdto.from(fb, tutor);
			return fbdto;
		}).collect(Collectors.toList());

		GetTutorFeedbacksResponseDto response = new GetTutorFeedbacksResponseDto();
		response.setFeedbacks(dtos);

		return response;
	}

	private LessonItemDao lessonItemDao;

	private FeedbackDao feedbackDao;

	private TodoDao todoDao;

	private UserDao userDao;

	public LessonItemDao getLessonItemDao() {
		return lessonItemDao;
	}

	public void setLessonItemDao(LessonItemDao lessonItemDao) {
		this.lessonItemDao = lessonItemDao;
	}

	public FeedbackDao getFeedbackDao() {
		return feedbackDao;
	}

	public void setFeedbackDao(FeedbackDao feedbackDao) {
		this.feedbackDao = feedbackDao;
	}

	public TodoDao getTodoDao() {
		return todoDao;
	}

	public void setTodoDao(TodoDao todoDao) {
		this.todoDao = todoDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

}

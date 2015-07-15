package org.harper.otms.calendar.service.impl;

import org.harper.otms.calendar.dao.ClientDao;
import org.harper.otms.calendar.dao.LessonDao;
import org.harper.otms.calendar.dao.TutorDao;
import org.harper.otms.calendar.entity.Lesson;
import org.harper.otms.calendar.entity.Lesson.Status;
import org.harper.otms.calendar.entity.OneoffEntry;
import org.harper.otms.calendar.entity.RepeatEntry;
import org.harper.otms.calendar.entity.Tutor;
import org.harper.otms.calendar.service.CalendarService;
import org.harper.otms.calendar.service.ErrorCode;
import org.harper.otms.calendar.service.dto.CancelLessonDto;
import org.harper.otms.calendar.service.dto.ConfirmCancelDto;
import org.harper.otms.calendar.service.dto.GetCalendarEventDto;
import org.harper.otms.calendar.service.dto.GetCalendarEventResponseDto;
import org.harper.otms.calendar.service.dto.SetupLessonDto;
import org.harper.otms.calendar.service.util.DateUtil;
import org.harper.otms.common.service.DataException;

public class DefaultCalendarService implements CalendarService {

	@Override
	public GetCalendarEventResponseDto getCalendarEvents(
			GetCalendarEventDto request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setupLesson(SetupLessonDto request) {
		Lesson lesson = new Lesson();
		lesson.setStatus(Status.REQUESTED);
		// This function generate 1-1 lessons
		lesson.setCapacity(1);

		if (request.getLessonId() != -1) {
			lesson = getLessonDao().findById(request.getLessonId());
		}

		Tutor tutor = getTutorDao().findByName(request.getTutorName());
		if (tutor == null) {
			throw new DataException(ErrorCode.TUTOR_NOT_FOUND);
		}
		lesson.setTutor(tutor);
		lesson.setClient(getClientDao().findById(request.getClientId()));

		if (request.isRepeat()) {
			RepeatEntry entry = new RepeatEntry();
			entry.setStartDate(request.getRepeatFromDate());
			entry.setStopDate(request.getRepeatToDate());
			entry.setDateExpression(DateUtil.weekRepeat(request.getWeekRepeat()));
			lesson.setCalendar(entry);
		} else {
			OneoffEntry entry = new OneoffEntry();
			entry.setDate(request.getOneoffDate());
			lesson.setCalendar(entry);
		}
		lesson.getCalendar().setStartTime(request.getFromTime().total());
		lesson.getCalendar().setStopTime(request.getToTime().total());

		getLessonDao().save(lesson);
	}

	@Override
	public void cancelLesson(CancelLessonDto request) {
		// TODO Auto-generated method stub

	}

	@Override
	public void confirmCancel(ConfirmCancelDto request) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateSnapshot() {
		// TODO Auto-generated method stub

	}

	private LessonDao lessonDao;

	private ClientDao clientDao;

	private TutorDao tutorDao;

	public LessonDao getLessonDao() {
		return lessonDao;
	}

	public void setLessonDao(LessonDao lessonDao) {
		this.lessonDao = lessonDao;
	}

	public ClientDao getClientDao() {
		return clientDao;
	}

	public void setClientDao(ClientDao clientDao) {
		this.clientDao = clientDao;
	}

	public TutorDao getTutorDao() {
		return tutorDao;
	}

	public void setTutorDao(TutorDao tutorDao) {
		this.tutorDao = tutorDao;
	}

}

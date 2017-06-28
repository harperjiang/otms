package org.harper.otms.calendar.service.impl;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.harper.otms.auth.dao.UserDao;
import org.harper.otms.auth.entity.User;
import org.harper.otms.calendar.dao.ClientDao;
import org.harper.otms.calendar.dao.LessonDao;
import org.harper.otms.calendar.dao.LessonItemDao;
import org.harper.otms.calendar.dao.TutorDao;
import org.harper.otms.calendar.entity.lesson.Lesson;
import org.harper.otms.calendar.entity.lesson.LessonItem;
import org.harper.otms.calendar.service.CalendarService;
import org.harper.otms.calendar.service.dto.EventDto;
import org.harper.otms.calendar.service.dto.GetCalendarEventDto;
import org.harper.otms.calendar.service.dto.GetCalendarEventResponseDto;
import org.harper.otms.calendar.service.util.DateUtil;

public class DefaultCalendarService implements CalendarService {

	@Override
	public GetCalendarEventResponseDto getEvents(GetCalendarEventDto request) {
		User user = getUserDao().findById(request.getCurrentUser());

		// Use custom time zone conversion instead of DWR's
		TimeZone utc = TimeZone.getTimeZone("UTC");

		Date fromDate = DateUtil.convert(request.getFromDate(), user.getTimezone(), utc);
		Date toDate = DateUtil.convert(request.getToDate(), user.getTimezone(), utc);

		Date now = DateUtil.nowUTC();

		// The past
		List<LessonItem> snapshotItems = getLessonItemDao().findWithin(user, fromDate, now, LessonItem.Status.SNAPSHOT,
				null);
		// The new
		List<Lesson> lessons = getLessonDao().findWithin(user, now, toDate, Lesson.Status.VALID);

		GetCalendarEventResponseDto result = new GetCalendarEventResponseDto();

		snapshotItems.forEach((LessonItem item) -> result.addEvent(new EventDto(item)));

		lessons.stream().flatMap((Lesson lesson) -> EventDto.fromLesson(lesson, fromDate, toDate).stream())
				.sorted((EventDto a, EventDto b) -> {
					Date aDate = DateUtil.form(a.getDate(), a.getFromTime());
					Date bDate = DateUtil.form(b.getDate(), b.getFromTime());
					return aDate.compareTo(bDate);
				}).forEach((EventDto e) -> result.addEvent(e));

		result.convert(TimeZone.getTimeZone("UTC"), user.getTimezone());

		return result;
	}

	private LessonDao lessonDao;

	private ClientDao clientDao;

	private TutorDao tutorDao;

	private UserDao userDao;

	private LessonItemDao lessonItemDao;

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

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public LessonItemDao getLessonItemDao() {
		return lessonItemDao;
	}

	public void setLessonItemDao(LessonItemDao lessonItemDao) {
		this.lessonItemDao = lessonItemDao;
	}
}

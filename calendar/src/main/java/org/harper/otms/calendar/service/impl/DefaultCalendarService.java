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
import org.harper.otms.calendar.entity.Lesson;
import org.harper.otms.calendar.entity.LessonItem;
import org.harper.otms.calendar.entity.OneoffEntry;
import org.harper.otms.calendar.entity.RepeatEntry;
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

		Date fromDate = DateUtil.convert(request.getFromDate(),
				user.getTimezone(), utc);
		Date toDate = DateUtil.convert(request.getToDate(), user.getTimezone(),
				utc);

		Date now = new Date();

		// The past
		List<LessonItem> snapshotItems = getLessonItemDao().findWithin(user,
				fromDate, now, LessonItem.Status.SNAPSHOT, null);
		// The new
		List<Lesson> lessons = getLessonDao().findWithin(user, now, toDate,
				Lesson.Status.VALID);

		GetCalendarEventResponseDto result = new GetCalendarEventResponseDto();

		for (LessonItem item : snapshotItems) {
			result.addEvent(new EventDto(item));
		}

		for (Lesson lesson : lessons) {
			if (lesson.getCalendar() instanceof OneoffEntry) {
				OneoffEntry ofe = (OneoffEntry) lesson.getCalendar();
				result.addEvent(new EventDto(EventDto.LESSON, lesson.getId(),
						DateUtil.truncate(ofe.getFromTime()),
						lesson.getTitle(), DateUtil.extract(ofe.getFromTime()),
						DateUtil.extract(ofe.getToTime()), lesson.getTutor(),
						lesson.getClient()));
			}
			if (lesson.getCalendar() instanceof RepeatEntry) {
				RepeatEntry rpe = (RepeatEntry) lesson.getCalendar();
				for (Date date : rpe.matchIn(fromDate, toDate)) {
					if (lesson.getItems().containsKey(date)) {
						// Has Item on this day, use item to replace this
						// information
						LessonItem item = lesson.getItems().get(date);
						if (item.getStatus() != LessonItem.Status.DELETED) {
							EventDto event = new EventDto(item);
							if (event.within(fromDate, toDate))
								result.addEvent(event);
						}
					} else {
						EventDto event = new EventDto(EventDto.LESSON,
								lesson.getId(), date, lesson.getTitle(),
								rpe.getFromTime(), rpe.getToTime(),
								lesson.getTutor(), lesson.getClient());
						if (event.within(fromDate, toDate))
							result.addEvent(event);
					}
				}
			}
		}

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

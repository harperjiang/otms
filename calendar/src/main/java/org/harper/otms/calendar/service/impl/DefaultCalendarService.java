package org.harper.otms.calendar.service.impl;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.harper.otms.auth.dao.UserDao;
import org.harper.otms.auth.entity.User;
import org.harper.otms.calendar.dao.ClientDao;
import org.harper.otms.calendar.dao.LessonDao;
import org.harper.otms.calendar.dao.LessonItemDao;
import org.harper.otms.calendar.dao.SnapshotDao;
import org.harper.otms.calendar.dao.TutorDao;
import org.harper.otms.calendar.entity.Lesson;
import org.harper.otms.calendar.entity.LessonItem;
import org.harper.otms.calendar.entity.OneoffEntry;
import org.harper.otms.calendar.entity.RepeatEntry;
import org.harper.otms.calendar.entity.Snapshot;
import org.harper.otms.calendar.service.CalendarService;
import org.harper.otms.calendar.service.dto.EventDto;
import org.harper.otms.calendar.service.dto.GetCalendarEventDto;
import org.harper.otms.calendar.service.dto.GetCalendarEventResponseDto;
import org.harper.otms.calendar.service.util.DateUtil;

public class DefaultCalendarService implements CalendarService {

	@Override
	public GetCalendarEventResponseDto getEvents(GetCalendarEventDto request) {
		User user = getUserDao().findById(request.getCurrentUser());

		TimeZone utc = TimeZone.getTimeZone("UTC");

		Date fromDate = DateUtil.truncate(DateUtil.convert(
				request.getFromDate(), user.getTimezone(), utc));
		Date toDate = DateUtil.dayend(DateUtil.convert(request.getToDate(),
				user.getTimezone(), utc));

		List<Snapshot> snapshots = getSnapshotDao().findWithin(user, fromDate,
				toDate);
		List<Lesson> lessons = getLessonDao().findWithin(user, fromDate,
				toDate, new Lesson.Status[] { Lesson.Status.VALID });

		GetCalendarEventResponseDto result = new GetCalendarEventResponseDto();

		for (Snapshot sn : snapshots) {
			result.addEvent(new EventDto(EventDto.SNAPSHOT, sn.getId(), sn
					.getDate(), sn.getTitle(), sn.getStartTime(), sn
					.getStopTime()));
		}

		for (Lesson lesson : lessons) {
			if (lesson.getCalendar() instanceof OneoffEntry) {
				OneoffEntry ofe = (OneoffEntry) lesson.getCalendar();
				result.addEvent(new EventDto(EventDto.LESSON, lesson.getId(),
						ofe.getDate(), lesson.getTitle(), ofe.getStartTime(),
						ofe.getStopTime()));
			}
			if (lesson.getCalendar() instanceof RepeatEntry) {
				RepeatEntry rpe = (RepeatEntry) lesson.getCalendar();
				for (Date date : rpe.matchIn(fromDate, toDate)) {
					if (lesson.getItems().containsKey(date)) {
						// Has Item on this day, use item to replace this
						// information
						LessonItem item = lesson.getItems().get(date);
						result.addEvent(new EventDto(EventDto.LESSON_ITEM, item
								.getId(), date, item.getTitle(), item
								.getStartTime(), item.getStopTime()));
					} else {
						result.addEvent(new EventDto(EventDto.LESSON, lesson
								.getId(), date, lesson.getTitle(), rpe
								.getStartTime(), rpe.getStopTime()));
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

	private SnapshotDao snapshotDao;

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

	public SnapshotDao getSnapshotDao() {
		return snapshotDao;
	}

	public void setSnapshotDao(SnapshotDao snapshotDao) {
		this.snapshotDao = snapshotDao;
	}

}

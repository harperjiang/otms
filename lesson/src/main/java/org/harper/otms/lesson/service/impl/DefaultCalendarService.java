package org.harper.otms.lesson.service.impl;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.harper.otms.auth.dao.UserDao;
import org.harper.otms.auth.entity.User;
import org.harper.otms.common.util.DateUtil;
import org.harper.otms.lesson.dao.LessonDao;
import org.harper.otms.lesson.dao.LessonItemDao;
import org.harper.otms.lesson.entity.Lesson;
import org.harper.otms.lesson.entity.LessonItem;
import org.harper.otms.lesson.service.CalendarService;
import org.harper.otms.lesson.service.dto.EventDto;
import org.harper.otms.lesson.service.dto.GetCalendarEventDto;
import org.harper.otms.lesson.service.dto.GetCalendarEventResponseDto;
import org.harper.otms.lesson.service.dto.ViewTimesheetDto;
import org.harper.otms.lesson.service.dto.ViewTimesheetResponseDto;
import org.harper.otms.profile.dao.ClientDao;
import org.harper.otms.profile.dao.TutorDao;
import org.harper.otms.profile.entity.Tutor;
import org.harper.otms.profile.entity.setting.Timesheet;
import org.harper.otms.profile.service.dto.TimesheetDto;

public class DefaultCalendarService implements CalendarService {
	
	protected static final char TS_LESSON_MARK = '2';

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

	@Override
	public ViewTimesheetResponseDto viewTimesheet(ViewTimesheetDto request) {
		Tutor tutor = getTutorDao().findById(request.getTutorId());

		User viewer = request.getCurrentUser() == 0 ? tutor.getUser() : getUserDao().findById(request.getCurrentUser());

		Timesheet timesheet = tutor.getTimesheet();
		if (timesheet == null) {
			timesheet = new Timesheet();
		}

		final Timesheet tsref = timesheet;

		// Load lesson range
		if (request.getWeekStart() != null) {
			TimeZone utc = TimeZone.getTimeZone("UTC");
			Date fromDate = DateUtil.convert(request.getWeekStart(), viewer.getTimezone(), utc);
			Date toDate = DateUtil.offset(fromDate, 7);

			// The events in the indicated week
			List<Lesson> lessons = getLessonDao().findWithin(tutor.getUser(), fromDate, toDate, Lesson.Status.VALID);
			lessons.stream().flatMap((Lesson lesson) -> EventDto.fromLesson(lesson, fromDate, toDate).stream())
					.sorted((EventDto a, EventDto b) -> {
						Date aDate = DateUtil.form(a.getDate(), a.getFromTime());
						Date bDate = DateUtil.form(b.getDate(), b.getFromTime());
						return aDate.compareTo(bDate);
					}).forEach((EventDto e) -> {
						// Mark the event on timesheet
						Date baseDate = DateUtil.toSunday(DateUtil.truncate(e.getDate()));
						int i = DateUtil.offset(e.getDate(), baseDate);
						int j0 = (int) Math.floor(e.getFromTime() / 30);
						int j1 = (int) Math.ceil(e.getToTime() / 30);
						if (!tsref.getValues().containsKey(baseDate)) {
							tsref.getValues().put(baseDate, tsref.getDefaultValue());
						}
						StringBuilder valueb = new StringBuilder();
						valueb.append(tsref.getValues().get(baseDate));

						for (int j = j0; j <= j1; j++) {
							valueb.setCharAt(i * 48 + j, TS_LESSON_MARK);
						}
						tsref.getValues().put(baseDate, valueb.toString());
					});
		}

		ViewTimesheetResponseDto result = new ViewTimesheetResponseDto();
		TimesheetDto tsDto = new TimesheetDto();
		tsDto.from(timesheet, viewer);

		result.setTimesheet(tsDto);
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

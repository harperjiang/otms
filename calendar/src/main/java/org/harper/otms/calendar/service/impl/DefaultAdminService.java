package org.harper.otms.calendar.service.impl;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.harper.otms.auth.dao.UserDao;
import org.harper.otms.auth.entity.User;
import org.harper.otms.calendar.dao.ActionLogDao;
import org.harper.otms.calendar.dao.ClientDao;
import org.harper.otms.calendar.dao.LessonDao;
import org.harper.otms.calendar.dao.LessonItemDao;
import org.harper.otms.calendar.dao.SchedulerDao;
import org.harper.otms.calendar.dao.TutorDao;
import org.harper.otms.calendar.entity.Client;
import org.harper.otms.calendar.entity.Lesson;
import org.harper.otms.calendar.entity.Lesson.Status;
import org.harper.otms.calendar.entity.LessonItem;
import org.harper.otms.calendar.entity.Tutor;
import org.harper.otms.calendar.service.AdminService;
import org.harper.otms.calendar.service.ErrorCode;
import org.harper.otms.calendar.service.dto.AdminScheduleLessonDto;
import org.harper.otms.calendar.service.dto.AdminScheduleLessonResponseDto;
import org.harper.otms.calendar.service.dto.AdminSearchLessonDto;
import org.harper.otms.calendar.service.dto.AdminSearchLessonResponseDto;
import org.harper.otms.calendar.service.dto.AdminUpdateAccountDto;
import org.harper.otms.calendar.service.dto.AdminUpdateAccountResponseDto;
import org.harper.otms.calendar.service.dto.AdminUpdateLessonDto;
import org.harper.otms.calendar.service.dto.AdminUpdateLessonResponseDto;
import org.harper.otms.calendar.service.dto.EventDto;
import org.harper.otms.calendar.service.util.DateUtil;
import org.harper.otms.common.service.DataException;

public class DefaultAdminService implements AdminService {

	User checkAdmin(int user) {
		User admin = getUserDao().findById(user);

		if (admin == null || !admin.getType().equals(User.TYPE_ADMIN)) {
			throw new DataException(ErrorCode.SYSTEM_NO_AUTH);
		}
		return admin;
	}

	@Override
	public AdminScheduleLessonResponseDto scheduleLesson(AdminScheduleLessonDto request) {
		/*
		 * 
		 * 1. get tutor information 2. get client information 3. create a lesson
		 * object 4. set the lesson relationship with tutor 5. set the lesson
		 * relationship with client
		 * 
		 * condition that need to check: 1. tutor name must be correct 2. client
		 * name must be correct 3. class start time must not earlier than
		 * current time 4. class end time must not earlier than start time&
		 * current time
		 * 
		 */
		User admin = checkAdmin(request.getCurrentUser());
		Lesson lesson = new Lesson();
		lesson.setRequestDate(DateUtil.nowUTC());

		request.getLesson().to(lesson, admin);
		Date firstDate = lesson.getCalendar().firstTime();
		if (firstDate == null) {
			return new AdminScheduleLessonResponseDto(ErrorCode.LESSON_INVALID_SCHEDULE);
		}
		if (firstDate.compareTo(lesson.getRequestDate()) < 0) {
			return new AdminScheduleLessonResponseDto(ErrorCode.LESSON_IN_PAST);
		}

		lesson.setCapacity(1);

		Tutor tutor = getTutorDao().findByName(request.getLesson().getTutorName());

		if (tutor == null) {
			return new AdminScheduleLessonResponseDto(ErrorCode.TUTOR_NOT_FOUND);
		}
		lesson.setTutor(tutor);

		Client client = getClientDao().findByName(request.getLesson().getClientName());
		if (client == null) {
			return new AdminScheduleLessonResponseDto(ErrorCode.SYSTEM_DATA_NOT_FOUND);
		}
		lesson.setClient(client);

		lesson.setStatus(Status.VALID);
		getLessonDao().save(lesson);

		// Setup Scheduler
		getSchedulerDao().setupScheduler(lesson);
		return new AdminScheduleLessonResponseDto();
	}

	@Override
	public AdminSearchLessonResponseDto searchLessons(AdminSearchLessonDto request) {
		User admin = checkAdmin(request.getCurrentUser());
		Tutor tutor = getTutorDao().findByName(request.getTutorName());

		TimeZone utc = TimeZone.getTimeZone("UTC");
		Date fromDate = DateUtil.convert(request.getFromDate(), admin.getTimezone(), utc);
		Date toDate = DateUtil.convert(request.getToDate(), admin.getTimezone(), utc);

		Date now = DateUtil.nowUTC();
		if (tutor == null) {
			throw new DataException(ErrorCode.TUTOR_NOT_FOUND);
		}
		Client client = getClientDao().findByName(request.getClientName());
		if (client == null) {
			throw new DataException(ErrorCode.SYSTEM_DATA_NOT_FOUND);
		}
		Date utcnow = DateUtil.nowUTC();
		List<LessonItem> items = getLessonItemDao().findWithin(tutor, client, fromDate, utcnow);
		List<Lesson> lessons = getLessonDao().findWithin(tutor, client, utcnow, toDate, Status.VALID);

		Stream<EventDto> event4Items = items.stream().map((LessonItem item) -> new EventDto(item));
		Stream<EventDto> event4Lessons = lessons.stream()
				.flatMap((Lesson lesson) -> EventDto.fromLesson(lesson, utcnow, toDate).stream());

		AdminSearchLessonResponseDto response = new AdminSearchLessonResponseDto();
		response.getEvents().addAll(event4Items.collect(Collectors.toList()));
		response.getEvents().addAll(event4Lessons.collect(Collectors.toList()));
		return response;
	}

	@Override
	public AdminUpdateLessonResponseDto updateLesson(AdminUpdateLessonDto request) {
		User admin = checkAdmin(request.getCurrentUser());
		return null;
	}

	@Override
	public AdminUpdateAccountResponseDto updateAccount(AdminUpdateAccountDto request) {
		// TODO Auto-generated method stub
		return null;
	}

	private UserDao userDao;

	private LessonDao lessonDao;

	private LessonItemDao lessonItemDao;

	private ActionLogDao actionLogDao;

	private SchedulerDao schedulerDao;

	private TutorDao tutorDao;

	private ClientDao clientDao;

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public LessonDao getLessonDao() {
		return lessonDao;
	}

	public void setLessonDao(LessonDao lessonDao) {
		this.lessonDao = lessonDao;
	}

	public LessonItemDao getLessonItemDao() {
		return lessonItemDao;
	}

	public void setLessonItemDao(LessonItemDao lessonItemDao) {
		this.lessonItemDao = lessonItemDao;
	}

	public ActionLogDao getActionLogDao() {
		return actionLogDao;
	}

	public void setActionLogDao(ActionLogDao actionLogDao) {
		this.actionLogDao = actionLogDao;
	}

	public SchedulerDao getSchedulerDao() {
		return schedulerDao;
	}

	public void setSchedulerDao(SchedulerDao schedulerDao) {
		this.schedulerDao = schedulerDao;
	}

	public TutorDao getTutorDao() {
		return tutorDao;
	}

	public void setTutorDao(TutorDao tutorDao) {
		this.tutorDao = tutorDao;
	}

	public ClientDao getClientDao() {
		return clientDao;
	}

	public void setClientDao(ClientDao clientDao) {
		this.clientDao = clientDao;
	}

}

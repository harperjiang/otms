package org.harper.otms.calendar.service.impl;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.harper.otms.auth.dao.UserDao;
import org.harper.otms.auth.entity.User;
import org.harper.otms.calendar.dao.ActionLogDao;
import org.harper.otms.calendar.dao.ClientDao;
import org.harper.otms.calendar.dao.LessonDao;
import org.harper.otms.calendar.dao.LessonItemDao;
import org.harper.otms.calendar.dao.SnapshotDao;
import org.harper.otms.calendar.dao.TutorDao;
import org.harper.otms.calendar.entity.ActionLog;
import org.harper.otms.calendar.entity.CalendarEntry;
import org.harper.otms.calendar.entity.Client;
import org.harper.otms.calendar.entity.Lesson;
import org.harper.otms.calendar.entity.Lesson.Status;
import org.harper.otms.calendar.entity.LessonItem;
import org.harper.otms.calendar.entity.OneoffEntry;
import org.harper.otms.calendar.entity.RepeatEntry;
import org.harper.otms.calendar.entity.Tutor;
import org.harper.otms.calendar.service.ErrorCode;
import org.harper.otms.calendar.service.LessonService;
import org.harper.otms.calendar.service.dto.CancelLessonDto;
import org.harper.otms.calendar.service.dto.ChangeLessonStatusDto;
import org.harper.otms.calendar.service.dto.ChangeLessonStatusResponseDto;
import org.harper.otms.calendar.service.dto.ConfirmCancelDto;
import org.harper.otms.calendar.service.dto.GetLessonDto;
import org.harper.otms.calendar.service.dto.GetLessonItemDto;
import org.harper.otms.calendar.service.dto.GetLessonItemResponseDto;
import org.harper.otms.calendar.service.dto.GetLessonResponseDto;
import org.harper.otms.calendar.service.dto.GetRequestedLessonDto;
import org.harper.otms.calendar.service.dto.GetRequestedLessonResponseDto;
import org.harper.otms.calendar.service.dto.LessonDto;
import org.harper.otms.calendar.service.dto.LessonItemDto;
import org.harper.otms.calendar.service.dto.MakeLessonItemDto;
import org.harper.otms.calendar.service.dto.MakeLessonItemResponseDto;
import org.harper.otms.calendar.service.dto.SetupLessonDto;
import org.harper.otms.calendar.service.dto.SetupLessonResponseDto;
import org.harper.otms.calendar.service.dto.TriggerLessonDto;
import org.harper.otms.calendar.service.dto.TriggerLessonResponseDto;
import org.harper.otms.calendar.service.util.DateUtil;
import org.quartz.CronScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.InitializingBean;

public class DefaultLessonService implements LessonService, InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		getScheduler().addJob(new TriggerLessonJobDetail(), true);
	}

	@Override
	public GetLessonResponseDto getLesson(GetLessonDto request) {
		GetLessonResponseDto result = new GetLessonResponseDto();

		User viewer = getUserDao().findById(request.getCurrentUser());

		Lesson lesson = getLessonDao().findById(request.getLessonId());
		// Check whether current user is owner
		if (null == lesson)
			return new GetLessonResponseDto(ErrorCode.DATA_NOT_FOUND);

		if (lesson.getClient().getId() != request.getCurrentUser()
				&& lesson.getTutor().getId() != request.getCurrentUser())
			return new GetLessonResponseDto(ErrorCode.SYS_NO_AUTH);

		if (lesson.getClient().getId() != request.getCurrentUser()) {
			result.setOwner(false);
		} else {
			result.setOwner(true);
		}

		result.setLesson(new LessonDto());
		result.getLesson().from(lesson, viewer);
		return result;
	}

	@Override
	public GetLessonItemResponseDto getLessonItem(GetLessonItemDto request) {
		GetLessonItemResponseDto result = new GetLessonItemResponseDto();
		User viewer = getUserDao().findById(request.getCurrentUser());

		LessonItem item = getLessonItemDao()
				.findById(request.getLessonItemId());
		if (null == item)
			return new GetLessonItemResponseDto(ErrorCode.DATA_NOT_FOUND);

		if (item.getLesson().getClient().getId() != request.getCurrentUser()
				&& item.getLesson().getTutor().getId() != request
						.getCurrentUser())
			return new GetLessonItemResponseDto(ErrorCode.SYS_NO_AUTH);

		if (item.getLesson().getClient().getId() != request.getCurrentUser()) {
			result.setOwner(false);
		} else {
			result.setOwner(true);
		}

		result.setLessonItem(new LessonItemDto());
		result.getLessonItem().from(item, viewer);
		return result;
	}

	@Override
	public MakeLessonItemResponseDto makeLessonItem(MakeLessonItemDto request) {
		// TODO Auto-generated method stub
		return null;
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
	public SetupLessonResponseDto setupLesson(SetupLessonDto request) {
		Lesson lesson = new Lesson();
		lesson.setRequestDate(DateUtil.convert(new Date(),
				TimeZone.getDefault(), TimeZone.getTimeZone("UTC")));
		User owner = getUserDao().findById(request.getCurrentUser());
			
		if (request.getLesson().getLessonId() != 0) {
			lesson = getLessonDao().findById(request.getLesson().getLessonId());
		} else {
			lesson.setStatus(Status.REQUESTED);
			// This function generate 1-1 lessons
			lesson.setCapacity(1);
		}

		Tutor tutor = getTutorDao().findByName(
				request.getLesson().getTutorName());
		if (tutor == null) {
			SetupLessonResponseDto result = new SetupLessonResponseDto();
			result.setErrorCode(ErrorCode.TUTOR_NOT_FOUND);
			return result;
		}
		lesson.setTutor(tutor);

		Client client = getClientDao().findById(request.getCurrentUser());
		if (client == null) {
			SetupLessonResponseDto result = new SetupLessonResponseDto();
			result.setErrorCode(ErrorCode.SYS_NO_USER);
			return result;
		}
		lesson.setClient(client);

		request.getLesson().to(lesson, owner);
		getLessonDao().save(lesson);

		return new SetupLessonResponseDto();
	}

	@Override
	public GetRequestedLessonResponseDto getRequestedLessons(
			GetRequestedLessonDto request) {
		User user = getUserDao().findById(request.getCurrentUser());
		List<Lesson> lessons = getLessonDao().findRequested(user);

		LessonDto[] dtos = new LessonDto[lessons.size()];

		for (int i = 0; i < lessons.size(); i++) {
			dtos[i] = new LessonDto();
			dtos[i].from(lessons.get(i), user);
		}

		GetRequestedLessonResponseDto result = new GetRequestedLessonResponseDto();
		result.setLessons(dtos);

		return result;
	}

	@Override
	public ChangeLessonStatusResponseDto changeLessonStatus(
			ChangeLessonStatusDto request) {
		Lesson lesson = getLessonDao().findById(request.getLessonId());
		Status toStatus = Status.valueOf(request.getToStatus());

		// Verify that user can do this operation
		if (lesson.getStatus() == Status.REQUESTED) {
			if (lesson.getTutor().getId() != request.getCurrentUser()) {
				return new ChangeLessonStatusResponseDto(ErrorCode.SYS_NO_AUTH);
			}
		}

		// Generate an actionlog for the operation
		ActionLog actionLog = new ActionLog();
		actionLog.setActionDate(DateUtil.convert(new Date(),
				TimeZone.getDefault(), TimeZone.getTimeZone("GMT")));
		actionLog.setOperator(lesson.getTutor().getUser());
		actionLog.setType("LESSON STATUS");
		actionLog.setFrom(lesson.getStatus().name());
		actionLog.setTo(toStatus.name());
		getActionLogDao().save(actionLog);

		lesson.setStatus(toStatus);

		// Setup lesson scheduler and reminder scheduler
		String triggerId = MessageFormat.format("trigger_{0}_{1}", "lesson",
				Integer.toString(lesson.getId()));
		TriggerBuilder<Trigger> tBuilder = TriggerBuilder.newTrigger()
				.withIdentity(triggerId, "calendar")
				.forJob("triggerLessonJob", "calendar");
		Trigger trigger = null;
		if (lesson.getCalendar() instanceof OneoffEntry) {
			OneoffEntry oe = (OneoffEntry) lesson.getCalendar();
			trigger = tBuilder.startAt(oe.getFromTime()).build();
		} else {
			RepeatEntry re = (RepeatEntry) lesson.getCalendar();
			trigger = tBuilder
					.startAt(re.getStartDate())
					.endAt(DateUtil.dayend(re.getStopDate()))
					.withSchedule(
							CronScheduleBuilder.cronSchedule(re.cronExp()))
					.build();
		}
		trigger.getJobDataMap().put(TriggerLessonJobDetail.LESSON_ID,
				String.valueOf(lesson.getId()));
		TriggerKey tkey = trigger.getKey();
		try {
			if (getScheduler().checkExists(tkey)) {
				getScheduler().rescheduleJob(tkey, trigger);
			} else {
				getScheduler().scheduleJob(trigger);
			}
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}

		return new ChangeLessonStatusResponseDto();
	}

	@Override
	public TriggerLessonResponseDto triggerLesson(TriggerLessonDto request) {
		if (request.getLessonItemId() != 0) {
			LessonItem item = getLessonItemDao().findById(
					request.getLessonItemId());
			item.setStatus(LessonItem.Status.SNAPSHOT);
		} else {
			Lesson lesson = getLessonDao().findById(request.getLessonId());
			CalendarEntry entry = lesson.getCalendar();

			Date today = DateUtil.truncate(new Date());

			LessonItem item = new LessonItem();
			item.setLesson(lesson);
			item.setTitle(lesson.getTitle());
			item.setDescription(lesson.getDescription());
			item.setMaskDate(today);
			item.setStatus(LessonItem.Status.SNAPSHOT);
			if (entry instanceof OneoffEntry) {
				OneoffEntry oe = (OneoffEntry) entry;
				item.setFromTime(oe.getFromTime());
				item.setToTime(oe.getToTime());
			} else {
				RepeatEntry re = (RepeatEntry) entry;
				item.setFromTime(DateUtil.form(today, re.getFromTime()));
				item.setToTime(DateUtil.form(today, re.getToTime()));
			}
			lesson.getItems().put(today, item);

			if (request.isLast()) {
				lesson.setStatus(Lesson.Status.INVALID);
				// Generate an actionlog for the operation
				ActionLog actionLog = new ActionLog();
				actionLog.setActionDate(DateUtil.convert(new Date(),
						TimeZone.getDefault(), TimeZone.getTimeZone("GMT")));
				actionLog.setOperator(null);
				actionLog.setType("LESSON STATUS");
				actionLog.setFrom(Lesson.Status.VALID.name());
				actionLog.setTo(Lesson.Status.INVALID.name());
				getActionLogDao().save(actionLog);
			}

		}
		return new TriggerLessonResponseDto();
	}

	private LessonDao lessonDao;

	private ClientDao clientDao;

	private TutorDao tutorDao;

	private LessonItemDao lessonItemDao;

	private SnapshotDao snapshotDao;

	private UserDao userDao;

	private ActionLogDao actionLogDao;

	private Scheduler scheduler;

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

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public ActionLogDao getActionLogDao() {
		return actionLogDao;
	}

	public void setActionLogDao(ActionLogDao actionLogDao) {
		this.actionLogDao = actionLogDao;
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

}

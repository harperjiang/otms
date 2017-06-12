package org.harper.otms.calendar.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.harper.otms.auth.dao.UserDao;
import org.harper.otms.auth.entity.User;
import org.harper.otms.calendar.dao.ActionLogDao;
import org.harper.otms.calendar.dao.ClientDao;
import org.harper.otms.calendar.dao.LessonDao;
import org.harper.otms.calendar.dao.LessonItemDao;
import org.harper.otms.calendar.dao.SchedulerDao;
import org.harper.otms.calendar.dao.TodoDao;
import org.harper.otms.calendar.dao.TutorDao;
import org.harper.otms.calendar.entity.ActionLog;
import org.harper.otms.calendar.entity.CalendarEntry;
import org.harper.otms.calendar.entity.Client;
import org.harper.otms.calendar.entity.Lesson;
import org.harper.otms.calendar.entity.Lesson.Status;
import org.harper.otms.calendar.entity.LessonItem;
import org.harper.otms.calendar.entity.OneoffEntry;
import org.harper.otms.calendar.entity.RepeatEntry;
import org.harper.otms.calendar.entity.Todo;
import org.harper.otms.calendar.entity.Tutor;
import org.harper.otms.calendar.service.ErrorCode;
import org.harper.otms.calendar.service.LessonService;
import org.harper.otms.calendar.service.dto.CancelLessonItemDto;
import org.harper.otms.calendar.service.dto.CancelLessonItemResponseDto;
import org.harper.otms.calendar.service.dto.ChangeLessonStatusDto;
import org.harper.otms.calendar.service.dto.ChangeLessonStatusResponseDto;
import org.harper.otms.calendar.service.dto.GetLessonDto;
import org.harper.otms.calendar.service.dto.GetLessonHistoryDto;
import org.harper.otms.calendar.service.dto.GetLessonHistoryResponseDto;
import org.harper.otms.calendar.service.dto.GetLessonItemDto;
import org.harper.otms.calendar.service.dto.GetLessonItemResponseDto;
import org.harper.otms.calendar.service.dto.GetLessonResponseDto;
import org.harper.otms.calendar.service.dto.GetOngoingLessonDto;
import org.harper.otms.calendar.service.dto.GetOngoingLessonResponseDto;
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
import org.harper.otms.common.service.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class DefaultLessonService implements LessonService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public GetLessonResponseDto getLesson(GetLessonDto request) {
		GetLessonResponseDto result = new GetLessonResponseDto();

		User viewer = getUserDao().findById(request.getCurrentUser());

		Lesson lesson = getLessonDao().findById(request.getLessonId());
		// Check whether current user is owner
		if (null == lesson)
			return new GetLessonResponseDto(ErrorCode.SYSTEM_DATA_NOT_FOUND);

		if (!viewer.isAdmin() && !lesson.isOwner(viewer))
			return new GetLessonResponseDto(ErrorCode.SYSTEM_NO_AUTH);

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

		LessonItem item = getLessonItemDao().findById(request.getLessonItemId());
		if (null == item)
			return new GetLessonItemResponseDto(ErrorCode.SYSTEM_DATA_NOT_FOUND);

		if (!viewer.isAdmin() && !item.getLesson().isOwner(viewer))
			return new GetLessonItemResponseDto(ErrorCode.SYSTEM_NO_AUTH);

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
	public GetLessonHistoryResponseDto getLessonHistory(GetLessonHistoryDto request) {
		User owner = getUserDao().findById(request.getCurrentUser());

		TimeZone fromzone = owner.getTimezone();
		TimeZone tozone = TimeZone.getTimeZone("GMT");

		List<LessonItem> records = getLessonItemDao().findWithin(owner,
				DateUtil.convert(request.getFromTime(), fromzone, tozone),
				DateUtil.convert(request.getToTime(), fromzone, tozone), LessonItem.Status.SNAPSHOT,
				request.getPaging());

		GetLessonHistoryResponseDto response = new GetLessonHistoryResponseDto();

		List<LessonItemDto> results = new ArrayList<LessonItemDto>();
		for (LessonItem li : records) {
			LessonItemDto dto = new LessonItemDto();
			dto.from(li, owner);
			results.add(dto);
		}

		response.setPaging(request.getPaging());
		response.setResult(results);

		return response;
	}

	@Override
	public SetupLessonResponseDto setupLesson(SetupLessonDto request) {
		Lesson lesson = new Lesson();
		lesson.setRequestDate(DateUtil.nowUTC());
		User owner = getUserDao().findById(request.getCurrentUser());

		if (request.getLesson().getLessonId() != 0) {
			lesson = getLessonDao().findById(request.getLesson().getLessonId());
			if (!lesson.getItems().isEmpty()) {
				return new SetupLessonResponseDto(ErrorCode.LESSON_IN_PROGRESS);
			}
		} else {
			lesson.setStatus(Status.REQUESTED);
			// This function generate 1-1 lessons
			lesson.setCapacity(1);
		}

		Tutor tutor = getTutorDao().findByName(request.getLesson().getTutorName());
		if (tutor == null) {
			return new SetupLessonResponseDto(ErrorCode.TUTOR_NOT_FOUND);
		}

		Client client = getClientDao().findById(request.getCurrentUser());
		if (client == null) {
			return new SetupLessonResponseDto(ErrorCode.SYSTEM_NO_USER);
		}

		lesson.setTutor(tutor);
		lesson.setClient(client);

		request.getLesson().to(lesson, owner);
		Date firstDate = lesson.getCalendar().firstTime();
		if (firstDate == null) {
			throw new DataException(ErrorCode.LESSON_INVALID_SCHEDULE);
		}
		if (firstDate.compareTo(lesson.getRequestDate()) < 0) {
			// Can only schedule a future lesson
			throw new DataException(ErrorCode.LESSON_IN_PAST);
		}

		getLessonDao().save(lesson);
		return new SetupLessonResponseDto();
	}

	protected LessonItem createLessonItem(Lesson lesson, Date from, Date to) {

		return null;
	}

	@Override
	public MakeLessonItemResponseDto makeLessonItem(MakeLessonItemDto request) {
		User owner = getUserDao().findById(request.getCurrentUser());

		LessonItem item = null;
		// Find the lesson item if exists
		if (!StringUtils.isEmpty(request.getLessonItemId())) {
			item = getLessonItemDao().findById(request.getLessonItemId());
			if (item == null)
				return new MakeLessonItemResponseDto(ErrorCode.SYSTEM_DATA_NOT_FOUND);
		} else if (!StringUtils.isEmpty(request.getLessonId())) {
			// Create one if not exists
			Lesson lesson = getLessonDao().findById(request.getLessonId());
			if (lesson == null)
				return new MakeLessonItemResponseDto(ErrorCode.SYSTEM_DATA_NOT_FOUND);
			item = new LessonItem();

			item.setStatus(LessonItem.Status.VALID);

			int fromTime = 0;
			if (lesson.getCalendar() instanceof OneoffEntry) {
				fromTime = DateUtil.extract(((OneoffEntry) lesson.getCalendar()).getFromTime());
			} else if (lesson.getCalendar() instanceof RepeatEntry) {
				fromTime = ((RepeatEntry) lesson.getCalendar()).getFromTime();
			}
			Date itemDate = DateUtil.form(request.getLessonItem().getDate(), fromTime);
			Date utcItemDate = DateUtil
					.truncate(DateUtil.convert(itemDate, owner.getTimezone(), TimeZone.getTimeZone("UTC")));

			lesson.getItems().put(utcItemDate, item);
		}

		// Not owner
		if (!owner.isAdmin() && !item.getLesson().isOwner(owner)) {
			return new MakeLessonItemResponseDto(ErrorCode.SYSTEM_NO_AUTH);
		}
		// Time expired
		if (item.getFromTime().compareTo(new Date()) < 0) {
			return new MakeLessonItemResponseDto(ErrorCode.LESSON_ITEM_EXPIRED);
		}
		request.getLessonItem().to(item, owner);
		getLessonItemDao().save(item);
		// Update Scheduler
		getSchedulerDao().setupScheduler(item);

		MakeLessonItemResponseDto resp = new MakeLessonItemResponseDto();
		resp.setLessonItemId(item.getId());
		return resp;
	}

	@Override
	public TriggerLessonResponseDto triggerLesson(TriggerLessonDto request) {
		LessonItem item = null;
		if (request.getLessonItemId() != 0) {
			item = getLessonItemDao().findById(request.getLessonItemId());
			if (item.getStatus() != LessonItem.Status.VALID) {
				logger.info(MessageFormat.format("Lesson Item is invalid:{0}", item.getId()));
				return new TriggerLessonResponseDto();
			}
			item.setStatus(LessonItem.Status.SNAPSHOT);
		} else {
			Lesson lesson = getLessonDao().findById(request.getLessonId());
			if (lesson.getStatus() != Lesson.Status.VALID) {
				logger.info(MessageFormat.format("Lesson is invalid:{0}", lesson.getId()));
				return new TriggerLessonResponseDto();
			}
			CalendarEntry entry = lesson.getCalendar();

			Date today = DateUtil.truncate(DateUtil.nowUTC());

			if (lesson.getItems().containsKey(today)) {
				// Today has an item, the lesson should not be fired
				logger.info("Lesson is not triggered as an item exists");
				return new TriggerLessonResponseDto();
			}

			item = new LessonItem();
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
			item.setLesson(lesson);
			lesson.getItems().put(today, item);

			if (request.isLast()) {
				lesson.setStatus(Lesson.Status.INVALID);
				// Generate an actionlog for the operation
				ActionLog actionLog = new ActionLog();
				actionLog.setActionDate(
						DateUtil.convert(new Date(), TimeZone.getDefault(), TimeZone.getTimeZone("GMT")));
				actionLog.setOperator(null);
				actionLog.setType("LESSON STATUS");
				actionLog.setFrom(Lesson.Status.VALID.name());
				actionLog.setTo(Lesson.Status.INVALID.name());
				actionLog.setComment(String.valueOf(lesson.getId()));
				getActionLogDao().save(actionLog);
			}
		}
		// Assign Id
		getLessonItemDao().save(item);
		// Add an action log for triggering lesson
		ActionLog actionLog = new ActionLog();
		actionLog.setActionDate(DateUtil.convert(new Date(), TimeZone.getDefault(), TimeZone.getTimeZone("GMT")));
		actionLog.setOperator(null);
		actionLog.setType("LESSON TRIGGER");
		actionLog.setComment(String.valueOf(item.getLesson().getId()));
		getActionLogDao().save(actionLog);

		// Add a todo list for commenting this snapshot
		Todo todo = new Todo();
		todo.setType(Todo.Type.CLIENT_FEEDBACK);
		todo.setOwner(item.getLesson().getClient().getUser());
		todo.setExpireTime(DateUtil.offset(7));
		todo.setRefId(item.getId());
		todo.getContext().addProperty(Todo.DK_LESSON_TUTORID, item.getLesson().getTutor().getId());
		todo.getContext().addProperty(Todo.DK_LESSON_TITLE, item.getTitle());
		todo.getContext().addProperty(Todo.DK_LESSON_WITH, item.getLesson().getTutor().getUser().getName());
		todo.getContext().addProperty(Todo.DK_LESSON_FROM, item.getFromTime().getTime());
		todo.getContext().addProperty(Todo.DK_LESSON_TO, item.getToTime().getTime());
		getTodoDao().save(todo);

		return new TriggerLessonResponseDto();
	}

	@Override
	public CancelLessonItemResponseDto cancelLessonItem(CancelLessonItemDto request) {
		LessonItem item = getLessonItemDao().findById(request.getLessonItemId());
		if (item == null) {
			return new CancelLessonItemResponseDto(ErrorCode.SYSTEM_DATA_NOT_FOUND);
		}
		if (item.getStatus() != LessonItem.Status.VALID) {
			return new CancelLessonItemResponseDto(ErrorCode.LESSON_INVALID);
		}
		if (item.getLesson().getTutor().getId() != request.getCurrentUser()
				&& item.getLesson().getClient().getId() != request.getCurrentUser()) {
			return new CancelLessonItemResponseDto(ErrorCode.SYSTEM_NO_AUTH);
		}
		item.setStatus(LessonItem.Status.DELETED);
		getSchedulerDao().cancelScheduler(item);
		return new CancelLessonItemResponseDto();
	}

	@Override
	public GetRequestedLessonResponseDto getRequestedLessons(GetRequestedLessonDto request) {
		User user = getUserDao().findById(request.getCurrentUser());
		List<Lesson> lessons = getLessonDao().findByStatus(user, Status.REQUESTED);

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
	public GetOngoingLessonResponseDto getOngoingLessons(GetOngoingLessonDto request) {
		User user = getUserDao().findById(request.getCurrentUser());
		List<Lesson> lessons = getLessonDao().findByStatus(user, Status.VALID);

		List<LessonDto> dtos = new ArrayList<LessonDto>();

		for (int i = 0; i < lessons.size(); i++) {
			LessonDto dto = new LessonDto();
			dto.from(lessons.get(i), user);
			dtos.add(dto);
		}

		GetOngoingLessonResponseDto result = new GetOngoingLessonResponseDto();
		result.setLessons(dtos);

		return result;
	}

	@Override
	public ChangeLessonStatusResponseDto changeLessonStatus(ChangeLessonStatusDto request) {
		User user = getUserDao().findById(request.getCurrentUser());
		boolean isAdmin = User.TYPE_ADMIN.equals(user.getType());
		Lesson lesson = getLessonDao().findById(request.getLessonId());
		if (user == null || lesson == null)
			return new ChangeLessonStatusResponseDto(ErrorCode.SYSTEM_DATA_NOT_FOUND);
		Status fromStatus = lesson.getStatus();
		Status toStatus = Status.valueOf(request.getToStatus());

		// Verify that user can do this operation
		switch (lesson.getStatus()) {
		case REQUESTED:
			switch (toStatus) {
			case VALID:
				if (!isAdmin && lesson.getTutor().getId() != request.getCurrentUser()) {
					return new ChangeLessonStatusResponseDto(ErrorCode.SYSTEM_NO_AUTH);
				}
				Date currentTime = DateUtil.nowUTC();
				if (lesson.getCalendar().lastTime().compareTo(currentTime) < 0) {
					return new ChangeLessonStatusResponseDto(ErrorCode.LESSON_IN_PAST);
				}
				break;
			case INVALID:
				if (!isAdmin && lesson.getTutor().getId() != request.getCurrentUser()
						&& lesson.getClient().getId() != request.getCurrentUser()) {
					return new ChangeLessonStatusResponseDto(ErrorCode.SYSTEM_NO_AUTH);
				}
				break;
			default:
				break;
			}
			break;
		case INVALID:
			if (toStatus != Status.INVALID) {
				return new ChangeLessonStatusResponseDto(ErrorCode.SYSTEM_NO_AUTH);
			}
			break;
		case VALID:
			switch (toStatus) {
			case VALID:
				break;
			case INVALID:
				if (!isAdmin && lesson.getTutor().getId() != request.getCurrentUser()
						&& lesson.getClient().getId() != request.getCurrentUser()) {
					return new ChangeLessonStatusResponseDto(ErrorCode.SYSTEM_NO_AUTH);
				}
				break;
			default:
				return new ChangeLessonStatusResponseDto(ErrorCode.SYSTEM_NO_AUTH);
			}
			break;
		default:
			break;
		}

		// Generate an actionlog for the operation
		ActionLog actionLog = new ActionLog();
		actionLog.setActionDate(DateUtil.convert(new Date(), TimeZone.getDefault(), TimeZone.getTimeZone("GMT")));
		actionLog.setOperator(user);
		actionLog.setType("LESSON STATUS");
		actionLog.setFrom(lesson.getStatus().name());
		actionLog.setTo(toStatus.name());
		actionLog.setComment(String.valueOf(lesson.getId()));
		getActionLogDao().save(actionLog);

		lesson.setStatus(toStatus);

		if (fromStatus == Status.REQUESTED && toStatus == Status.VALID) {
			// Setup Scheduler
			getSchedulerDao().setupScheduler(lesson);
		}
		if (fromStatus == Status.VALID && toStatus == Status.INVALID) {
			// Cancel Scheduler
			getSchedulerDao().cancelScheduler(lesson);
		}

		return new ChangeLessonStatusResponseDto();
	}

	private LessonDao lessonDao;

	private ClientDao clientDao;

	private TutorDao tutorDao;

	private LessonItemDao lessonItemDao;

	private UserDao userDao;

	private ActionLogDao actionLogDao;

	private TodoDao todoDao;

	private SchedulerDao schedulerDao;

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

	public TodoDao getTodoDao() {
		return todoDao;
	}

	public void setTodoDao(TodoDao todoDao) {
		this.todoDao = todoDao;
	}

	public SchedulerDao getSchedulerDao() {
		return schedulerDao;
	}

	public void setSchedulerDao(SchedulerDao schedulerDao) {
		this.schedulerDao = schedulerDao;
	}

}

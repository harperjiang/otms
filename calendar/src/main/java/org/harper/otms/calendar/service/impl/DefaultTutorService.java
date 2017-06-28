package org.harper.otms.calendar.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.harper.otms.auth.dao.UserDao;
import org.harper.otms.auth.entity.User;
import org.harper.otms.auth.service.ErrorCode;
import org.harper.otms.calendar.dao.LessonDao;
import org.harper.otms.calendar.dao.TutorDao;
import org.harper.otms.calendar.entity.lesson.Lesson;
import org.harper.otms.calendar.entity.profile.Tutor;
import org.harper.otms.calendar.entity.setting.Timesheet;
import org.harper.otms.calendar.service.TutorService;
import org.harper.otms.calendar.service.dto.EventDto;
import org.harper.otms.calendar.service.dto.FindTutorDto;
import org.harper.otms.calendar.service.dto.FindTutorResponseDto;
import org.harper.otms.calendar.service.dto.GetPopularTutorDto;
import org.harper.otms.calendar.service.dto.GetPopularTutorResponseDto;
import org.harper.otms.calendar.service.dto.GetTimesheetDto;
import org.harper.otms.calendar.service.dto.GetTimesheetResponseDto;
import org.harper.otms.calendar.service.dto.GetTutorInfoDto;
import org.harper.otms.calendar.service.dto.GetTutorInfoResponseDto;
import org.harper.otms.calendar.service.dto.SetupTimesheetDto;
import org.harper.otms.calendar.service.dto.SetupTimesheetResponseDto;
import org.harper.otms.calendar.service.dto.SetupTutorDto;
import org.harper.otms.calendar.service.dto.SetupTutorResponseDto;
import org.harper.otms.calendar.service.dto.TimesheetDto;
import org.harper.otms.calendar.service.dto.TutorBriefDto;
import org.harper.otms.calendar.service.dto.TutorInfoDto;
import org.harper.otms.calendar.service.util.DateUtil;
import org.harper.otms.common.service.DataException;

public class DefaultTutorService implements TutorService {

	protected static final char TS_LESSON_MARK = '2';

	@Override
	public GetTimesheetResponseDto getTimesheet(GetTimesheetDto request) {
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

		GetTimesheetResponseDto result = new GetTimesheetResponseDto();
		TimesheetDto tsDto = new TimesheetDto();
		tsDto.from(timesheet, viewer);

		result.setTimesheet(tsDto);
		return result;
	}

	@Override
	public SetupTimesheetResponseDto setupTimesheet(SetupTimesheetDto request) {

		Tutor tutor = getTutorDao().findById(request.getCurrentUser());
		if (tutor == null)
			throw new DataException(ErrorCode.SYSTEM_DATA_NOT_FOUND);

		Timesheet timesheet = tutor.getTimesheet();
		if (null == timesheet) {
			timesheet = new Timesheet();
			tutor.setTimesheet(timesheet);
		}
		request.getTimesheet().to(timesheet, tutor.getUser());

		return new SetupTimesheetResponseDto();
	}

	@Override
	public GetPopularTutorResponseDto getPopularTutors(GetPopularTutorDto request) {
		GetPopularTutorResponseDto response = new GetPopularTutorResponseDto();

		List<Tutor> populars = getTutorDao().findPopular();
		List<TutorBriefDto> tbs = new ArrayList<TutorBriefDto>();
		for (Tutor t : populars) {
			TutorBriefDto tbd = new TutorBriefDto();
			tbd.from(t);
			tbs.add(tbd);
		}
		response.setTutors(tbs);

		return response;
	}

	@Override
	public FindTutorResponseDto findTutors(FindTutorDto request) {
		FindTutorResponseDto response = new FindTutorResponseDto();

		return response;
	}

	@Override
	public GetTutorInfoResponseDto getTutorInfo(GetTutorInfoDto request) {
		Tutor tutor = getTutorDao().findById(request.getTutorId());

		TutorInfoDto infoDto = new TutorInfoDto();
		infoDto.from(tutor);

		GetTutorInfoResponseDto result = new GetTutorInfoResponseDto();
		result.setTutorInfo(infoDto);
		return result;
	}

	@Override
	public SetupTutorResponseDto setupTutor(SetupTutorDto request) {
		if (request.getCurrentUser() != request.getTutorInfo().getTutorId()) {
			return new SetupTutorResponseDto(ErrorCode.SYSTEM_NO_AUTH);
		}
		Tutor tutor = getTutorDao().findById(request.getCurrentUser());
		request.getTutorInfo().to(tutor);
		return new SetupTutorResponseDto();
	}

	private TutorDao tutorDao;

	private UserDao userDao;

	private LessonDao lessonDao;

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

	public LessonDao getLessonDao() {
		return lessonDao;
	}

	public void setLessonDao(LessonDao lessonDao) {
		this.lessonDao = lessonDao;
	}

}

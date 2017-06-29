package org.harper.otms.profile.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.harper.otms.auth.dao.UserDao;
import org.harper.otms.auth.entity.User;
import org.harper.otms.auth.service.ErrorCode;
import org.harper.otms.common.service.DataException;
import org.harper.otms.profile.dao.TutorDao;
import org.harper.otms.profile.entity.Tutor;
import org.harper.otms.profile.entity.setting.Timesheet;
import org.harper.otms.profile.service.TutorService;
import org.harper.otms.profile.service.dto.FindTutorDto;
import org.harper.otms.profile.service.dto.FindTutorResponseDto;
import org.harper.otms.profile.service.dto.GetPopularTutorDto;
import org.harper.otms.profile.service.dto.GetPopularTutorResponseDto;
import org.harper.otms.profile.service.dto.GetTimesheetDto;
import org.harper.otms.profile.service.dto.GetTimesheetResponseDto;
import org.harper.otms.profile.service.dto.GetTutorInfoDto;
import org.harper.otms.profile.service.dto.GetTutorInfoResponseDto;
import org.harper.otms.profile.service.dto.SetupTimesheetDto;
import org.harper.otms.profile.service.dto.SetupTimesheetResponseDto;
import org.harper.otms.profile.service.dto.SetupTutorDto;
import org.harper.otms.profile.service.dto.SetupTutorResponseDto;
import org.harper.otms.profile.service.dto.TimesheetDto;
import org.harper.otms.profile.service.dto.TutorBriefDto;
import org.harper.otms.profile.service.dto.TutorInfoDto;

public class DefaultTutorService implements TutorService {

	@Override
	public GetTimesheetResponseDto getTimesheet(GetTimesheetDto request) {
		Tutor tutor = getTutorDao().findById(request.getTutorId());

		User viewer = request.getCurrentUser() == 0 ? tutor.getUser() : getUserDao().findById(request.getCurrentUser());

		Timesheet timesheet = tutor.getTimesheet();
		if (timesheet == null) {
			timesheet = new Timesheet();
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

}

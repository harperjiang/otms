package org.harper.otms.calendar.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.harper.otms.calendar.dao.TutorDao;
import org.harper.otms.calendar.entity.Timesheet;
import org.harper.otms.calendar.entity.Tutor;
import org.harper.otms.calendar.service.TutorService;
import org.harper.otms.calendar.service.dto.FindTutorDto;
import org.harper.otms.calendar.service.dto.FindTutorResponseDto;
import org.harper.otms.calendar.service.dto.GetTimesheetDto;
import org.harper.otms.calendar.service.dto.GetTimesheetResponseDto;
import org.harper.otms.calendar.service.dto.SetupTimesheetDto;
import org.harper.otms.calendar.service.dto.SetupTimesheetResponseDto;

public class DefaultTutorService implements TutorService {

	@Override
	public GetTimesheetResponseDto getTimesheet(GetTimesheetDto request) {

		Tutor tutor = getTutorDao().findById(request.getTutorId());
		Timesheet timesheet = tutor.getTimesheet();

		GetTimesheetResponseDto result = new GetTimesheetResponseDto();

		String[] exps = new String[7];
		exps[0] = timesheet.getSundayExpression();
		exps[1] = timesheet.getMondayExpression();
		exps[2] = timesheet.getTuesdayExpression();
		exps[3] = timesheet.getWednesdayExpression();
		exps[4] = timesheet.getThursdayExpression();
		exps[5] = timesheet.getFridayExpression();
		exps[6] = timesheet.getSaturdayExpression();

		result.setExpressions(exps);

		result.setHolidays(new Date[tutor.getHolidays().size()]);
		tutor.getHolidays().toArray(result.getHolidays());
		return result;
	}

	@Override
	public SetupTimesheetResponseDto setupTimesheet(SetupTimesheetDto request) {

		Tutor tutor = getTutorDao().findById(request.getTutorId());
		Timesheet timesheet = tutor.getTimesheet();

		String[] exps = request.getExpressions();
		if (exps != null) {
			timesheet.setSundayExpression(exps[0]);
			timesheet.setMondayExpression(exps[1]);
			timesheet.setTuesdayExpression(exps[2]);
			timesheet.setWednesdayExpression(exps[3]);
			timesheet.setThursdayExpression(exps[4]);
			timesheet.setFridayExpression(exps[5]);
			timesheet.setSaturdayExpression(exps[6]);
		}
		if (request.getHolidays() != null) {
			List<Date> holidays = new ArrayList<Date>();
			for (Date holiday : request.getHolidays()) {
				holidays.add(holiday);
			}
			tutor.setHolidays(holidays);
		}

		SetupTimesheetResponseDto result = new SetupTimesheetResponseDto();
		return result;
	}

	@Override
	public FindTutorResponseDto findTutors(FindTutorDto request) {
		// TODO Auto-generated method stub
		return null;
	}

	private TutorDao tutorDao;

	public TutorDao getTutorDao() {
		return tutorDao;
	}

	public void setTutorDao(TutorDao tutorDao) {
		this.tutorDao = tutorDao;
	}

}

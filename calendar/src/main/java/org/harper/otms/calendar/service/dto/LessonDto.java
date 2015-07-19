package org.harper.otms.calendar.service.dto;

import java.util.Date;
import java.util.TimeZone;

import org.harper.otms.auth.entity.User;
import org.harper.otms.calendar.entity.CalendarEntry;
import org.harper.otms.calendar.entity.Lesson;
import org.harper.otms.calendar.entity.OneoffEntry;
import org.harper.otms.calendar.entity.RepeatEntry;
import org.harper.otms.calendar.entity.RepeatEntry.DateExpression;
import org.harper.otms.calendar.service.util.DateUtil;

public class LessonDto {

	int lessonId;

	String tutorName;

	int tutorId;

	String title;

	String description;

	TimeDto fromTime;

	TimeDto toTime;

	boolean repeat;

	Date repeatFromDate;

	Date repeatToDate;

	Date oneoffDate;

	boolean[] weekrepeat;

	String clientName;

	int clientId;

	public void from(Lesson lesson, User viewer) {
		setLessonId(lesson.getId());
		setTitle(lesson.getTitle());
		setDescription(lesson.getDescription());
		setClientName(lesson.getClient().getUser().getName());
		setClientId(lesson.getClient().getId());
		setTutorName(lesson.getTutor().getUser().getName());
		setTutorId(lesson.getTutor().getId());
		// Convert the dates to GMT
		// All time should be converted and stored in UTC
		lesson.getCalendar().convert(TimeZone.getTimeZone("UTC"),
				viewer.getTimezone());

		if (lesson.getCalendar() instanceof RepeatEntry) {
			setRepeat(true);
			RepeatEntry entry = (RepeatEntry) lesson.getCalendar();
			setRepeatFromDate(entry.getStartDate());
			setRepeatToDate(entry.getStopDate());

			setWeekrepeat(new DateExpression(entry.getDateExpression())
					.getWeek());
		} else {
			OneoffEntry entry = (OneoffEntry) lesson.getCalendar();
			setOneoffDate(entry.getDate());
		}
		setFromTime(new TimeDto(lesson.getCalendar().getStartTime()));
		setToTime(new TimeDto(lesson.getCalendar().getStopTime()));
	}

	public void to(Lesson lesson, User owner) {
		lesson.setTitle(getTitle());
		lesson.setDescription(getDescription());

		// Convert the dates to GMT
		if (isRepeat()) {
			RepeatEntry entry = new RepeatEntry();
			entry.setStartDate(getRepeatFromDate());
			entry.setStopDate(getRepeatToDate());

			entry.setDateExpression(DateUtil.weekRepeat(getWeekrepeat()));
			lesson.setCalendar(entry);
		} else {
			OneoffEntry entry = new OneoffEntry();
			entry.setDate(getOneoffDate());
			lesson.setCalendar(entry);
		}
		CalendarEntry entry = lesson.getCalendar();
		entry.setStartTime(getFromTime().total());
		entry.setStopTime(getToTime().total());

		// All time should be converted and stored in UTC
		entry.convert(owner.getTimezone(), TimeZone.getTimeZone("UTC"));
	}

	public String getTutorName() {
		return tutorName;
	}

	public void setTutorName(String tutorName) {
		this.tutorName = tutorName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TimeDto getFromTime() {
		return fromTime;
	}

	public void setFromTime(TimeDto fromTime) {
		this.fromTime = fromTime;
	}

	public TimeDto getToTime() {
		return toTime;
	}

	public void setToTime(TimeDto toTime) {
		this.toTime = toTime;
	}

	public boolean isRepeat() {
		return repeat;
	}

	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}

	public Date getRepeatFromDate() {
		return repeatFromDate;
	}

	public void setRepeatFromDate(Date repeatFromDate) {
		this.repeatFromDate = repeatFromDate;
	}

	public Date getRepeatToDate() {
		return repeatToDate;
	}

	public void setRepeatToDate(Date repeatToDate) {
		this.repeatToDate = repeatToDate;
	}

	public Date getOneoffDate() {
		return oneoffDate;
	}

	public void setOneoffDate(Date oneoffDate) {
		this.oneoffDate = oneoffDate;
	}

	public boolean[] getWeekrepeat() {
		return weekrepeat;
	}

	public void setWeekrepeat(boolean[] weekrepeat) {
		this.weekrepeat = weekrepeat;
	}

	public int getLessonId() {
		return lessonId;
	}

	public void setLessonId(int lessonId) {
		this.lessonId = lessonId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public int getTutorId() {
		return tutorId;
	}

	public void setTutorId(int tutorId) {
		this.tutorId = tutorId;
	}

}

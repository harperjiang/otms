package org.harper.otms.calendar.service.dto;

import java.util.Date;

import org.harper.otms.common.dto.RequestDto;

public class SetupLessonDto extends RequestDto {

	int lessonId = -1;

	int clientId;

	String tutorName;

	String title;

	String description;

	TimeDto fromTime;

	TimeDto toTime;

	boolean repeat;

	Date repeatFromDate;

	Date repeatToDate;

	Date oneoffDate;

	boolean[] weekRepeat;

	public int getLessonId() {
		return lessonId;
	}

	public void setLessonId(int lessonId) {
		this.lessonId = lessonId;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
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

	public boolean[] getWeekRepeat() {
		return weekRepeat;
	}

	public void setWeekRepeat(boolean[] weekRepeat) {
		this.weekRepeat = weekRepeat;
	}

}

package org.harper.otms.calendar.service.dto;

import java.util.Date;
import java.util.TimeZone;

import org.harper.otms.auth.entity.User;
import org.harper.otms.calendar.entity.lesson.LessonItem;
import org.harper.otms.calendar.service.util.DateUtil;

public class LessonItemDto {

	private int itemId;

	private String title;

	private int tutorId;

	private String tutorName;

	private int clientId;

	private String clientName;

	private String description;

	private Date date;

	private TimeDto fromTime;

	private TimeDto toTime;

	private String status;

	private String feedbackStatus;

	public void from(LessonItem item, User viewer) {
		setItemId(item.getId());
		setTitle(item.getTitle());
		setTutorName(item.getLesson().getTutor().getUser().getName());
		setTutorId(item.getLesson().getTutor().getId());
		setClientName(item.getLesson().getClient().getUser().getName());
		setClientId(item.getLesson().getClient().getId());
		setDescription(item.getDescription());

		TimeZone utc = TimeZone.getTimeZone("UTC");
		item.convert(utc, viewer.getTimezone());

		setDate(DateUtil.truncate(item.getFromTime()));

		setFromTime(new TimeDto(DateUtil.extract(item.getFromTime())));
		setToTime(new TimeDto(DateUtil.extract(item.getToTime())));

		setStatus(item.getStatus().name());
		setFeedbackStatus(item.getFeedbackStatus().name());
	}

	public void to(LessonItem item, User owner) {
		TimeZone utc = TimeZone.getTimeZone("UTC");

		item.setTitle(getTitle());
		item.setDescription(getDescription());

		item.setFromTime(DateUtil.form(getDate(), getFromTime().total()));
		item.setToTime(DateUtil.form(getDate(), getToTime().total()));

		/*
		 * Changing status directly with this method is not allowed
		 */
		/*
		 * item.setStatus(LessonItem.Status.valueOf(getStatus()));
		 * item.setFeedbackStatus
		 * (LessonItem.FeedbackStatus.valueOf(getFeedbackStatus()));
		 */
		item.convert(owner.getTimezone(), utc);

	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getTutorId() {
		return tutorId;
	}

	public void setTutorId(int tutorId) {
		this.tutorId = tutorId;
	}

	public String getTutorName() {
		return tutorName;
	}

	public void setTutorName(String tutorName) {
		this.tutorName = tutorName;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getFeedbackStatus() {
		return feedbackStatus;
	}

	public void setFeedbackStatus(String feedbackStatus) {
		this.feedbackStatus = feedbackStatus;
	}

}

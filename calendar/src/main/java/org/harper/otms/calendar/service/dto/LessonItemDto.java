package org.harper.otms.calendar.service.dto;

import java.util.Date;
import java.util.TimeZone;

import org.harper.otms.auth.entity.User;
import org.harper.otms.calendar.entity.LessonItem;
import org.harper.otms.calendar.service.util.DateUtil;

public class LessonItemDto {

	private int itemId;

	private String title;

	private String tutorName;

	private String description;

	private Date date;

	private int fromTime;

	private int toTime;

	private String status;

	public void from(LessonItem item, User viewer) {
		setItemId(item.getId());
		setTitle(item.getTitle());
		setTutorName(item.getLesson().getTutor().getUser().getName());
		setDescription(item.getDescription());

		TimeZone utc = TimeZone.getTimeZone("UTC");
		item.convert(utc, viewer.getTimezone());

		setDate(DateUtil.truncate(item.getFromTime()));
		setFromTime(DateUtil.extract(item.getFromTime()));
		setToTime(DateUtil.extract(item.getToTime()));
		setStatus(item.getStatus().name());
	}

	public void to(LessonItem item, User owner) {
		TimeZone utc = TimeZone.getTimeZone("UTC");

		item.setTitle(getTitle());
		item.setDescription(getDescription());
		// Do not contain tutor
		item.setFromTime(DateUtil.form(getDate(), getFromTime()));
		item.setToTime(DateUtil.form(getDate(), getToTime()));

		item.setStatus(LessonItem.Status.valueOf(getStatus()));
		item.convert(owner.getTimezone(), utc);

	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTutorName() {
		return tutorName;
	}

	public void setTutorName(String tutorName) {
		this.tutorName = tutorName;
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

	public int getFromTime() {
		return fromTime;
	}

	public void setFromTime(int fromTime) {
		this.fromTime = fromTime;
	}

	public int getToTime() {
		return toTime;
	}

	public void setToTime(int toTime) {
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

}

package org.harper.otms.calendar.service.dto;

import java.util.Date;
import java.util.TimeZone;

import org.harper.otms.auth.entity.User;
import org.harper.otms.calendar.entity.Snapshot;
import org.harper.otms.calendar.service.util.DateUtil;

public class SnapshotDto {

	private String title;

	private String tutorName;

	private String description;

	private Date date;

	private int fromTime;

	private int toTime;

	public void from(Snapshot item, User viewer) {
		setTitle(item.getTitle());
		setTutorName(item.getTutor().getUser().getName());
		setDescription(item.getDescription());

		TimeZone utc = TimeZone.getTimeZone("UTC");
		item.convert(utc, viewer.getTimezone());

		setDate(DateUtil.truncate(item.getFromTime()));
		setFromTime(DateUtil.extract(item.getFromTime()));
		setToTime(DateUtil.extract(item.getToTime()));
	}

	public void to(Snapshot item, User owner) {
		TimeZone utc = TimeZone.getTimeZone("UTC");

		item.setTitle(getTitle());
		item.setDescription(getDescription());
		// Do not contain tutor
		item.setFromTime(DateUtil.form(getDate(), getFromTime()));
		item.setToTime(DateUtil.form(getDate(), getToTime()));

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

}

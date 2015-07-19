package org.harper.otms.calendar.service.dto;

import java.util.Date;
import java.util.TimeZone;

import org.harper.otms.calendar.service.util.DateUtil;

public class EventDto {

	public static String SNAPSHOT = "snapshot";

	public static String LESSON_ITEM = "lesson_item";

	public static String LESSON = "lesson";

	private String type;

	private int id;

	private Date date;

	private String title;

	private int fromTime;

	private int toTime;

	public EventDto() {

	}

	public EventDto(String type, int id, Date date, String title, int fromTime,
			int toTime) {
		this.type = type;
		this.id = id;
		this.date = date;
		this.title = title;
		this.fromTime = fromTime;
		this.toTime = toTime;
	}

	public void convert(TimeZone from, TimeZone to) {
		Date newfromdates = DateUtil.form(getDate(), getFromTime());
		Date newtodates = DateUtil.form(getDate(), getToTime());

		setDate(DateUtil.truncate(newfromdates));
		setFromTime(DateUtil.extract(newfromdates));
		setToTime(DateUtil.extract(newtodates));
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

package org.harper.otms.calendar.service.dto;

import java.util.Date;
import java.util.TimeZone;

import org.harper.otms.calendar.entity.Client;
import org.harper.otms.calendar.entity.LessonItem;
import org.harper.otms.calendar.entity.Tutor;
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

	private String tutorName;

	private String clientName;

	private int tutorId;

	private int clientId;

	public EventDto() {

	}

	public EventDto(String type, int id, Date date, String title, int fromTime,
			int toTime, Tutor tutor, Client client) {
		this.type = type;
		this.id = id;
		this.date = date;
		this.title = title;
		this.fromTime = fromTime;
		this.toTime = toTime;
		this.tutorId = tutor.getId();
		this.tutorName = tutor.getUser().getName();
		this.clientId = client.getId();
		this.clientName = client.getUser().getName();
	}

	public EventDto(LessonItem item) {
		this(EventDto.LESSON_ITEM, item.getId(), DateUtil.truncate(item
				.getFromTime()), item.getTitle(), DateUtil.extract(item
				.getFromTime()), DateUtil.extract(item.getToTime()), item
				.getLesson().getTutor(), item.getLesson().getClient());
	}

	public void convert(TimeZone from, TimeZone to) {
		Date newfromdates = DateUtil.convert(
				DateUtil.form(getDate(), getFromTime()), from, to);
		Date newtodates = DateUtil.convert(
				DateUtil.form(getDate(), getToTime()), from, to);

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

	public String getTutorName() {
		return tutorName;
	}

	public void setTutorName(String tutorName) {
		this.tutorName = tutorName;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public int getTutorId() {
		return tutorId;
	}

	public void setTutorId(int tutorId) {
		this.tutorId = tutorId;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public boolean within(Date fromDate, Date toDate) {
		Date myfrom = DateUtil.form(getDate(), getFromTime());
		Date myto = DateUtil.form(getDate(), getToTime());
		return myto.compareTo(fromDate) > 0 && myfrom.compareTo(toDate) < 0;
	}

}

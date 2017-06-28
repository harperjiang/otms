package org.harper.otms.calendar.service.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.harper.otms.calendar.entity.lesson.Lesson;
import org.harper.otms.calendar.entity.lesson.LessonItem;
import org.harper.otms.calendar.entity.lesson.OneoffEntry;
import org.harper.otms.calendar.entity.lesson.RepeatEntry;
import org.harper.otms.calendar.entity.profile.Client;
import org.harper.otms.calendar.entity.profile.Tutor;
import org.harper.otms.calendar.service.util.DateUtil;

public class EventDto {

	public static String SNAPSHOT = "snapshot";

	public static String LESSON_ITEM = "lesson_item";

	public static String LESSON = "lesson";

	private String type;

	private boolean past;

	private int id;

	private Date date;

	private String title;

	private int fromTime;

	private int toTime;

	private String tutorName;

	private String clientName;

	private int tutorId;

	private int clientId;

	private boolean oneoff = false;

	public EventDto() {

	}

	public EventDto(String type, int id, Date date, String title, int fromTime, int toTime, Tutor tutor, Client client,
			boolean oneoff) {
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
		this.oneoff = oneoff;
	}

	public EventDto(LessonItem item) {
		this(EventDto.LESSON_ITEM, item.getId(), DateUtil.truncate(item.getFromTime()), item.getTitle(),
				DateUtil.extract(item.getFromTime()), DateUtil.extract(item.getToTime()), item.getLesson().getTutor(),
				item.getLesson().getClient(), false);
		setPast(item.getStatus() == LessonItem.Status.SNAPSHOT);
	}

	public void convert(TimeZone from, TimeZone to) {
		Date newfromdates = DateUtil.convert(DateUtil.form(getDate(), getFromTime()), from, to);
		Date newtodates = DateUtil.convert(DateUtil.form(getDate(), getToTime()), from, to);

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

	public boolean isPast() {
		return past;
	}

	public void setPast(boolean past) {
		this.past = past;
	}

	public boolean isOneoff() {
		return oneoff;
	}

	public void setOneoff(boolean oneoff) {
		this.oneoff = oneoff;
	}

	public boolean within(Date fromDate, Date toDate) {
		Date myfrom = DateUtil.form(getDate(), getFromTime());
		Date myto = DateUtil.form(getDate(), getToTime());
		return myto.compareTo(fromDate) > 0 && myfrom.compareTo(toDate) < 0;
	}

	public static List<EventDto> fromLesson(Lesson lesson, Date fromDate, Date toDate) {
		ArrayList<EventDto> result = new ArrayList<EventDto>();
		if (lesson.getCalendar() instanceof OneoffEntry) {
			OneoffEntry ofe = (OneoffEntry) lesson.getCalendar();
			result.add(new EventDto(EventDto.LESSON, lesson.getId(), DateUtil.truncate(ofe.getFromTime()),
					lesson.getTitle(), DateUtil.extract(ofe.getFromTime()), DateUtil.extract(ofe.getToTime()),
					lesson.getTutor(), lesson.getClient(), true));
		}
		if (lesson.getCalendar() instanceof RepeatEntry) {
			RepeatEntry rpe = (RepeatEntry) lesson.getCalendar();
			for (Date date : rpe.matchIn(fromDate, toDate)) {
				if (lesson.getItems().containsKey(date)) {
					// Has Item on this day, use item to replace this
					// information
					LessonItem item = lesson.getItems().get(date);
					if (item.getStatus() == LessonItem.Status.VALID) {
						EventDto event = new EventDto(item);
						if (event.within(fromDate, toDate))
							result.add(event);
					}
				} else {
					EventDto event = new EventDto(EventDto.LESSON, lesson.getId(), date, lesson.getTitle(),
							rpe.getFromTime(), rpe.getToTime(), lesson.getTutor(), lesson.getClient(), false);
					if (event.within(fromDate, toDate))
						result.add(event);
				}
			}
		}
		return result;
	}

}

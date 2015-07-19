package org.harper.otms.calendar.entity;

import java.util.Date;
import java.util.TimeZone;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.harper.otms.common.dao.Entity;

@javax.persistence.Entity
@Table(name = "lesson_item")
public class LessonItem extends Entity {

	public static enum Status {
		VALID, DELETED
	}

	@Column(name = "event_date")
	@Temporal(TemporalType.DATE)
	private Date date;

	@Column(name = "mask_date")
	@Temporal(TemporalType.DATE)
	private Date maskDate;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private Status status;

	@Column(name = "start_time")
	private int startTime;

	@Column(name = "stop_time")
	private int stopTime;

	@OneToOne
	@JoinColumn(name = "lesson_id")
	private Lesson lesson;

	@Column(name = "title")
	private String title;

	@Column(name = "description")
	private String description;

	public void convert(TimeZone from, TimeZone to) {
		OneoffEntry entry = new OneoffEntry();
		entry.setDate(date);
		entry.setStartTime(startTime);
		entry.setStopTime(stopTime);
		entry.convert(from, to);

		setDate(entry.getDate());
		setStartTime(entry.getStartTime());
		setStopTime(entry.getStopTime());
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getStopTime() {
		return stopTime;
	}

	public void setStopTime(int stopTime) {
		this.stopTime = stopTime;
	}

	public Lesson getLesson() {
		return lesson;
	}

	public void setLesson(Lesson lesson) {
		this.lesson = lesson;
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

	public Date getMaskDate() {
		return maskDate;
	}

	public void setMaskDate(Date maskDate) {
		this.maskDate = maskDate;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}

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

import org.harper.otms.calendar.service.util.DateUtil;
import org.harper.otms.common.dao.Entity;

@javax.persistence.Entity
@Table(name = "lesson_item")
public class LessonItem extends Entity {

	public static enum Status {
		VALID, DELETED, SNAPSHOT
	}

	/**
	 * Define which date in a event series this item masks. Must be in GMT and
	 * not converted by {@link #convert(TimeZone, TimeZone)}
	 */
	@Column(name = "mask_date")
	@Temporal(TemporalType.DATE)
	private Date maskDate;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private Status status;

	@Column(name = "from_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fromTime;

	@Column(name = "to_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date toTime;

	@OneToOne
	@JoinColumn(name = "lesson_id")
	private Lesson lesson;

	@Column(name = "title")
	private String title;

	@Column(name = "description")
	private String description;

	public void convert(TimeZone from, TimeZone to) {
		setFromTime(DateUtil.convert(getFromTime(), from, to));
		setToTime(DateUtil.convert(getToTime(), from, to));
	}

	public Date getFromTime() {
		return fromTime;
	}

	public void setFromTime(Date fromTime) {
		this.fromTime = fromTime;
	}

	public Date getToTime() {
		return toTime;
	}

	public void setToTime(Date toTime) {
		this.toTime = toTime;
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

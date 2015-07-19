package org.harper.otms.calendar.entity;

import java.util.Date;
import java.util.TimeZone;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.harper.otms.common.dao.Entity;

@javax.persistence.Entity
@Table(name = "snapshot")
public class Snapshot extends Entity {

	@Column(name = "event_date")
	@Temporal(TemporalType.DATE)
	private Date date;

	@Column(name = "start_time")
	private int startTime;

	@Column(name = "stop_time")
	private int stopTime;

	@OneToOne
	@JoinColumn(name = "tutor_id")
	private Tutor tutor;

	@OneToOne
	@JoinColumn(name = "client_id")
	private Client client;

	@Column(name = "title")
	private String title;

	@Column(name = "description")
	private String description;

	@Column(name = "tutor_comment")
	private String tutorComment;

	@Column(name = "client_comment")
	private String clientComment;

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

	public Tutor getTutor() {
		return tutor;
	}

	public void setTutor(Tutor tutor) {
		this.tutor = tutor;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public String getTutorComment() {
		return tutorComment;
	}

	public void setTutorComment(String tutorComment) {
		this.tutorComment = tutorComment;
	}

	public String getClientComment() {
		return clientComment;
	}

	public void setClientComment(String clientComment) {
		this.clientComment = clientComment;
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

}

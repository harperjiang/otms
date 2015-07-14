package org.harper.otms.calendar.entity;

import java.util.Date;

import org.harper.otms.common.dao.Entity;

public class CalendarSnapshot extends Entity {

	private Date date;

	private int startTime;

	private int stopTime;

	private Tutor tutor;

	private Client client;

	private String tutorComment;

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

}

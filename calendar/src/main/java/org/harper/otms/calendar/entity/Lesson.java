package org.harper.otms.calendar.entity;

import org.harper.otms.common.dao.Entity;

public class Lesson extends Entity {

	public static enum Status {
		REQUESTED, REJECTED, VALID, CANCELING, CANCELED
	}

	private Tutor tutor;

	private int capacity;

	private Client client;

	private Status status;

	private CalendarEntry calendar;

	public Tutor getTutor() {
		return tutor;
	}

	public void setTutor(Tutor tutor) {
		this.tutor = tutor;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public CalendarEntry getCalendar() {
		return calendar;
	}

	public void setCalendar(CalendarEntry calendar) {
		this.calendar = calendar;
	}

}

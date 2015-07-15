package org.harper.otms.calendar.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.harper.otms.common.dao.Entity;

@javax.persistence.Entity
@Table(name = "lesson")
public class Lesson extends Entity {

	public static enum Status {
		REQUESTED, REJECTED, VALID, CANCELING, CANCELED
	}

	@OneToOne
	@JoinColumn(name = "tutor_id")
	private Tutor tutor;

	@Column(name = "capacity")
	private int capacity;

	@OneToOne
	@JoinColumn(name = "client_id")
	private Client client;

	@Column(name = "title")
	private String title;

	@Column(name = "description")
	private String description;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private Status status;

	@OneToOne(orphanRemoval = true, cascade = { CascadeType.ALL })
	@JoinColumn(name = "calendar")
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

}

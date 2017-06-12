package org.harper.otms.calendar.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.harper.otms.common.dao.Entity;

@javax.persistence.Entity
@Table(name = "lesson")
public class Lesson extends Entity {

	// Invalid represents (CANCELLED, DELETED, EXPIRED, REJECTED)
	public static enum Status {
		REQUESTED, VALID, INVALID
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

	@Column(name = "request_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date requestDate;

	@OneToOne(orphanRemoval = true, cascade = { CascadeType.ALL })
	@JoinColumn(name = "calendar")
	private CalendarEntry calendar;

	// This date is the user date
	@OneToMany(mappedBy = "lesson", cascade = { CascadeType.ALL })
	@MapKey(name = "maskDate")
	private Map<Date, LessonItem> items = new HashMap<Date, LessonItem>();

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

	public Map<Date, LessonItem> getItems() {
		return items;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

}

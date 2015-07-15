package org.harper.otms.calendar.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.harper.otms.common.dao.Entity;

@javax.persistence.Entity
@Table(name = "calendar_entry")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="calendar_type")
public class CalendarEntry extends Entity {

	@Column(name = "start_time")
	private int startTime;

	@Column(name = "stop_time")
	private int stopTime;

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

}

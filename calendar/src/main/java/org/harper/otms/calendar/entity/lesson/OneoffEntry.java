package org.harper.otms.calendar.entity.lesson;

import java.util.Date;
import java.util.TimeZone;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.harper.otms.calendar.service.util.DateUtil;

@Entity
@DiscriminatorValue("ONEOFF")
public class OneoffEntry extends CalendarEntry {

	@Column(name = "oneoff_from_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fromTime;

	@Column(name = "oneoff_to_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date toTime;

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

	@Override
	public void convert(TimeZone from, TimeZone to) {
		setFromTime(DateUtil.convert(getFromTime(), from, to));
		setToTime(DateUtil.convert(getToTime(), from, to));
	}

	@Override
	public Date firstTime() {
		return getFromTime();
	}
	
	@Override
	public Date lastTime() {
		return getFromTime();
	}

}

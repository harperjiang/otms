package org.harper.otms.calendar.entity;

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

	@Column(name = "oneoff_date")
	@Temporal(TemporalType.DATE)
	private Date date;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public void convert(TimeZone from, TimeZone to) {
		Date fromDate = DateUtil.form(getDate(), getStartTime());
		Date toDate = DateUtil.form(getDate(), getStopTime());

		Date nfDate = DateUtil.convert(fromDate, from, to);
		Date ntDate = DateUtil.convert(toDate, from, to);

		setDate(DateUtil.truncate(nfDate));
		setStartTime(DateUtil.extract(nfDate));
		setStopTime(DateUtil.extract(ntDate));

	}

}

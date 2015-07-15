package org.harper.otms.calendar.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@DiscriminatorValue("REPEAT")
public class RepeatEntry extends CalendarEntry {

	@Column(name = "repeat_start_date")
	@Temporal(TemporalType.DATE)
	private Date startDate;

	@Column(name = "repeat_stop_date")
	@Temporal(TemporalType.DATE)
	private Date stopDate;

	/**
	 * Use a crontab like pattern
	 */
	@Column(name = "date_exp")
	private String dateExpression;

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getStopDate() {
		return stopDate;
	}

	public void setStopDate(Date stopDate) {
		this.stopDate = stopDate;
	}

	public String getDateExpression() {
		return dateExpression;
	}

	public void setDateExpression(String dateExpression) {
		this.dateExpression = dateExpression;
	}

}

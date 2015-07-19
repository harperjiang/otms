package org.harper.otms.calendar.entity;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.harper.otms.calendar.service.util.DateUtil;

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

	public List<Date> matchIn(Date from, Date to) {
		DateExpression de = new DateExpression(getDateExpression());
		List<Date> result = new ArrayList<Date>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(from);
		while (cal.getTime().compareTo(to) < 0) {
			Date current = cal.getTime();
			if (current.compareTo(getStartDate()) >= 0
					&& current.compareTo(getStopDate()) <= 0
					&& de.match(current)) {
				result.add(current);
			}
			cal.add(Calendar.DAY_OF_YEAR, 1);
		}
		return result;
	}

	@Override
	public void convert(TimeZone from, TimeZone to) {

		Date startTs = DateUtil.form(getStartDate(), getStartTime());

		Date stopStartTs = DateUtil.form(getStopDate(), getStartTime());

		Date stopTs = DateUtil.form(getStartDate(), getStopTime());

		Date newStartTs = DateUtil.convert(startTs, from, to);
		Date newStopTs = DateUtil.convert(stopTs, from, to);

		Date newStopStartTs = DateUtil.convert(stopStartTs, from, to);

		setStartDate(DateUtil.truncate(newStartTs));
		setStopDate(DateUtil.truncate(newStopStartTs));
		setStartTime(DateUtil.extract(newStartTs));
		setStopTime(DateUtil.extract(newStopTs));

		int offset = DateUtil.offset(newStartTs, startTs);

		// The date expression need to be updated if time difference is more
		// than one day
		if (offset != 0) {
			DateExpression dateExp = new DateExpression(getDateExpression());
			dateExp.shift(offset);
			setDateExpression(dateExp.toString());
		}
	}

	public static class DateExpression {

		private String[] weekExp;

		public DateExpression(String exp) {
			this.parse(exp);
		}

		void parse(String exp) {
			weekExp = exp.split("\t")[2].split(",");
		}

		public boolean match(Date date) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return "1".equals(weekExp[cal.get(Calendar.DAY_OF_WEEK) - 1]);
		}

		public void shift(int offset) {
			String[] newparts = new String[weekExp.length];
			for (int i = 0; i < weekExp.length; i++) {
				int nof = i + offset;
				while (nof < 0) {
					nof += weekExp.length;
				}
				newparts[nof % weekExp.length] = weekExp[i];
			}
			weekExp = newparts;
		}

		@Override
		public String toString() {
			return MessageFormat.format("\t\t{0}", String.join(",", weekExp));
		}

		public boolean[] getWeek() {
			boolean[] wee = new boolean[weekExp.length];
			for (int i = 0; i < weekExp.length; i++) {
				wee[i] = "1".equals(weekExp[i]);
			}
			return wee;
		}
	}
}

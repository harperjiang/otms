package org.harper.otms.lesson.entity;

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

import org.harper.otms.common.util.DateUtil;

@Entity
@DiscriminatorValue("REPEAT")
public class RepeatEntry extends CalendarEntry {

	@Column(name = "repeat_start_date")
	@Temporal(TemporalType.DATE)
	private Date startDate;

	@Column(name = "repeat_stop_date")
	@Temporal(TemporalType.DATE)
	private Date stopDate;

	@Column(name = "repeat_from_time")
	private int fromTime;

	@Column(name = "repeat_to_time")
	private int toTime;

	public int getFromTime() {
		return fromTime;
	}

	public void setFromTime(int fromTime) {
		this.fromTime = fromTime;
	}

	public int getToTime() {
		return toTime;
	}

	public void setToTime(int toTime) {
		this.toTime = toTime;
	}

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

	@Override
	public void convert(TimeZone from, TimeZone to) {

		Date startTs = DateUtil.form(getStartDate(), getFromTime());

		Date stopStartTs = DateUtil.form(getStopDate(), getFromTime());

		Date stopTs = DateUtil.form(getStartDate(), getToTime());

		Date newStartTs = DateUtil.convert(startTs, from, to);
		Date newStopTs = DateUtil.convert(stopTs, from, to);

		Date newStopStartTs = DateUtil.convert(stopStartTs, from, to);

		setStartDate(DateUtil.truncate(newStartTs));
		setStopDate(DateUtil.truncate(newStopStartTs));
		setFromTime(DateUtil.extract(newStartTs));
		setToTime(DateUtil.extract(newStopTs));

		int offset = DateUtil.dateDiff(newStartTs, startTs);

		// The date expression need to be updated if time difference is more
		// than one day
		if (offset != 0) {
			DateExpression dateExp = new DateExpression(getDateExpression());
			dateExp.shift(offset);
			setDateExpression(dateExp.toString());
		}
	}

	public static class DateExpression {

		private boolean[] week;

		public DateExpression(String exp) {
			this.parse(exp);
		}

		void parse(String exp) {
			String[] weekExp = exp.split("\t")[2].split(",");
			week = new boolean[weekExp.length];
			for (int i = 0; i < weekExp.length; i++) {
				if ("1".equals(weekExp[i])) {
					week[i] = true;
				} else {
					week[i] = false;
				}
			}
		}

		public boolean match(Date date) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return week[cal.get(Calendar.DAY_OF_WEEK) - 1];
		}

		public void shift(int offset) {
			boolean[] newparts = new boolean[week.length];
			for (int i = 0; i < week.length; i++) {
				int nof = i + offset;
				while (nof < 0) {
					nof += week.length;
				}
				newparts[nof % week.length] = week[i];
			}
			week = newparts;
		}

		@Override
		public String toString() {
			StringBuilder str = new StringBuilder();
			for (boolean w : week) {
				str.append(w ? "1" : "0").append(",");
			}
			str.deleteCharAt(str.length() - 1);
			return MessageFormat.format("\t\t{0}", str.toString());
		}

		public boolean[] getWeek() {
			return week;
		}
	}

	static final String[] CRON_WEEK = { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };

	// Follow Quartz format
	public String cronExp() {
		int hour = getFromTime() / 60;
		int min = getFromTime() % 60;

		boolean[] weeks = new DateExpression(getDateExpression()).getWeek();
		StringBuilder week = new StringBuilder();
		for (int i = 0; i < 7; i++) {
			if (weeks[i]) {
				week.append(CRON_WEEK[i]).append(",");
			}
		}
		week.deleteCharAt(week.length() - 1);

		String cron = MessageFormat.format("0 {0} {1} ? * {2} *", min, hour, week.toString());

		return cron;
	}

	public List<Date> matchIn(Date from, Date to) {
		DateExpression de = new DateExpression(getDateExpression());
		List<Date> result = new ArrayList<Date>();
		Calendar cal = Calendar.getInstance();

		Date since = from.compareTo(getStartDate()) >= 0 ? from : getStartDate();
		Date until = to.compareTo(getStopDate()) >= 0 ? getStopDate() : to;

		cal.setTime(since);
		while (cal.getTime().compareTo(until) <= 0) {
			Date current = cal.getTime();
			if (de.match(current)) {
				result.add(current);
			}
			cal.add(Calendar.DAY_OF_YEAR, 1);
		}
		return result;
	}

	@Override
	public Date firstTime() {
		// Find first valid day
		DateExpression de = new DateExpression(getDateExpression());
		Calendar cal = Calendar.getInstance();
		cal.setTime(getStartDate());
		while (cal.getTime().compareTo(getStopDate()) <= 0) {
			Date current = cal.getTime();
			if (de.match(current)) {
				return DateUtil.form(current, getFromTime());
			}
			cal.add(Calendar.DAY_OF_YEAR, 1);
		}
		return null;
	}

	@Override
	public Date lastTime() {
		// Find last valid day
		DateExpression de = new DateExpression(getDateExpression());
		Calendar cal = Calendar.getInstance();
		cal.setTime(getStopDate());
		while (cal.getTime().compareTo(getStartDate()) >= 0) {
			Date current = cal.getTime();
			if (de.match(current)) {
				return DateUtil.form(current, getFromTime());
			}
			cal.add(Calendar.DAY_OF_YEAR, -1);
		}
		return null;
	}
}

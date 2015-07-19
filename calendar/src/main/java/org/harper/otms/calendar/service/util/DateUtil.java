package org.harper.otms.calendar.service.util;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.Days;

public class DateUtil {

	public static String weekRepeat(boolean[] week) {
		StringBuilder sb = new StringBuilder();
		for (boolean val : week) {
			sb.append(val ? 1 : 0);
			sb.append(',');
		}
		sb.deleteCharAt(sb.length() - 1);
		return MessageFormat.format("\t\t{0}", sb.toString());
	}

	public static Date truncate(Date time) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static Date dayend(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}

	/**
	 * This convert function convert the literal representation of a date based
	 * on given time zone.
	 * 
	 * E.g. 2015-01-01 00:00 convert('GMT', 'Asia/Shanghai') will give you a
	 * date with local string representation '2015-01-01 08:00' no matter what
	 * local time zone is.
	 * 
	 * @param date
	 * @param from
	 * @param to
	 * @return
	 */
	public static Date convert(Date date, TimeZone from, TimeZone to) {
		// We assume the input date is always a date in local time zone, thus if
		// local time zone is not equivalent to from time zone, a field to field
		// copy conversion is needed
		if (TimeZone.getDefault() != from) {
			date = datewithtz(date, from);
		}
		Calendar cal = Calendar.getInstance(from);
		cal.setTime(date);
		cal.setTimeZone(to);
		DateFormat format = new SimpleDateFormat("yyyy-M-d H:m");
		String timeString = MessageFormat.format("{0}-{1}-{2} {3}:{4}",
				Integer.toString(cal.get(Calendar.YEAR)),
				cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH),
				cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
		return format.parse(timeString, new ParsePosition(0));
	}

	public static Date form(Date date, int time) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, time / 60);
		cal.set(Calendar.MINUTE, time % 60);
		// Call to getTime triggers calculation
		return cal.getTime();

	}

	/**
	 * This function copy a date from local time-zone to another, they have the
	 * same literal. E.g 2015-01-01 00:00 US/Eastern to 2015-01-01 00:00 UTC
	 * 
	 * @param input
	 * @param origintz
	 * @param newtz
	 * @return
	 */
	public static Date datewithtz(Date input, TimeZone newtz) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(input);
		String localString = MessageFormat.format("{0}-{1}-{2} {3}:{4}",
				Integer.toString(cal.get(Calendar.YEAR)),
				cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH),
				cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		df.setTimeZone(newtz);
		return df.parse(localString, new ParsePosition(0));
	}

	public static int offset(Date newDate, Date ref) {
		return Days.daysBetween(new DateTime(ref), new DateTime(newDate))
				.getDays();
	}

	public static int extract(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);
	}
}

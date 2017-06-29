package org.harper.otms.common.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.harper.otms.common.util.DateUtil;
import org.junit.Test;

public class DateUtilTest extends DateUtil {

	@Test
	public void testConvert() {
		DateFormat sdf = new SimpleDateFormat("yyyy-M-d HH:mm");
		Date date = sdf.parse("2015-08-01 21:00", new ParsePosition(0));
		Date togo = sdf.parse("2015-08-02 05:00", new ParsePosition(0));

		Date converted = DateUtil.convert(date, TimeZone.getTimeZone("UTC"), TimeZone.getTimeZone("Asia/Shanghai"));
		assertEquals(togo, converted);

		converted = DateUtil.convert(date, TimeZone.getTimeZone("Asia/Shanghai"), TimeZone.getTimeZone("US/Central"));
		togo = sdf.parse("2015-08-01 08:00", new ParsePosition(0));
		assertEquals(togo, converted);
	}

	@Test
	public void testCal() {
		TimeZone from = TimeZone.getTimeZone("US/Eastern");
		TimeZone to = TimeZone.getTimeZone("GMT");

		Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2015-07-21", new ParsePosition(0));
		int startTime = 840;
		Calendar startCal = Calendar.getInstance(from);

		startCal.setTime(startDate);
		int startHour = startTime / 60;
		int startMin = startTime % 60;

		startCal.set(Calendar.HOUR_OF_DAY, startHour);
		startCal.set(Calendar.MINUTE, startMin);

		startCal.getTime();

		startCal.setTimeZone(to);

		System.out.println(startCal.get(Calendar.HOUR_OF_DAY) * 60 + startCal.get(Calendar.MINUTE));

	}

	@Test
	public void testToSunday() throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = df.parse("2017-06-26", new ParsePosition(0));
		assertEquals(df.parse("2017-06-25"),DateUtil.toSunday(date));
	}
}

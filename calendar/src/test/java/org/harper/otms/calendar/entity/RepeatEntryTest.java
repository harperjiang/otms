package org.harper.otms.calendar.entity;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

public class RepeatEntryTest extends RepeatEntry {

	@Test
	public void testConvert() {
		RepeatEntry re = new RepeatEntry();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		re.setStartDate(df.parse("2015-07-19", new ParsePosition(0)));
		re.setStopDate(df.parse("2015-07-31", new ParsePosition(0)));
		re.setFromTime(840); // 14:00
		re.setToTime(960); // 16:00

		re.convert(TimeZone.getTimeZone("US/Eastern"),
				TimeZone.getTimeZone("UTC"));

		assertEquals(1080, re.getFromTime());
		assertEquals(1200, re.getToTime());
		assertEquals(df.parse("2015-07-19", new ParsePosition(0)),
				re.getStartDate());
		assertEquals(df.parse("2015-07-31", new ParsePosition(0)),
				re.getStopDate());
	}

	@Test
	public void testCronExp() {
		RepeatEntry re = new RepeatEntry();
		re.setFromTime(850);
		re.setDateExpression("\t\t0,0,1,1,0,1,1");
		assertEquals("0 10 14 ? * TUE,WED,FRI,SAT *", re.cronExp());
	}

	@Test
	public void testDate() {
		Date date = new Date(1437708600000l);
		System.out.println(date);
	}
}

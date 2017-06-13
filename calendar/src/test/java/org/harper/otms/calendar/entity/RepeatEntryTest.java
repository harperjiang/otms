package org.harper.otms.calendar.entity;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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

		re.convert(TimeZone.getTimeZone("US/Eastern"), TimeZone.getTimeZone("UTC"));

		assertEquals(1080, re.getFromTime());
		assertEquals(1200, re.getToTime());
		assertEquals(df.parse("2015-07-19", new ParsePosition(0)), re.getStartDate());
		assertEquals(df.parse("2015-07-31", new ParsePosition(0)), re.getStopDate());
	}

	@Test
	public void testConvertBetweenDay() {
		RepeatEntry re = new RepeatEntry();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		re.setStartDate(df.parse("2017-07-01", new ParsePosition(0)));
		re.setStopDate(df.parse("2017-07-30", new ParsePosition(0)));
		re.setFromTime(1380); // 23:00
		re.setToTime(1410); // 23:30
		// MWF
		re.setDateExpression("\t\t0,1,0,1,0,1,0");

		re.convert(TimeZone.getTimeZone("US/Central"), TimeZone.getTimeZone("UTC"));

		assertEquals(240, re.getFromTime());
		assertEquals(270, re.getToTime());

		assertEquals(df.parse("2017-07-02", new ParsePosition(0)), re.getStartDate());
		assertEquals(df.parse("2017-07-31", new ParsePosition(0)), re.getStopDate());
		assertEquals("\t\t0,0,1,0,1,0,1", re.getDateExpression());
	}

	@Test
	public void testCronExp() {
		RepeatEntry re = new RepeatEntry();
		re.setFromTime(850);
		re.setDateExpression("\t\t0,0,1,1,0,1,1");
		assertEquals("0 10 14 ? * TUE,WED,FRI,SAT *", re.cronExp());
	}

	@Test
	public void testMatchIn() throws ParseException {
		RepeatEntry re = new RepeatEntry();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		re.setStartDate(df.parse("2017-06-12 00:00", new ParsePosition(0)));
		re.setStopDate(df.parse("2017-06-15 00:00", new ParsePosition(0)));
		re.setFromTime(1380); // 23:00
		re.setToTime(1410); // 23:30
		re.setDateExpression("\t\t1,0,0,1,0,0,1");

		List<Date> dates = re.matchIn(df.parse("2010-01-01 00:00"), df.parse("2019-01-01 00:00"));
		assertEquals(1, dates.size());
	}

	@Test
	public void testFirstTime() {
		RepeatEntry re = new RepeatEntry();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		re.setStartDate(df.parse("2017-06-12 00:00", new ParsePosition(0)));
		re.setStopDate(df.parse("2017-06-15 00:00", new ParsePosition(0)));
		re.setFromTime(1380); // 23:00
		re.setToTime(1410); // 23:30
		re.setDateExpression("\t\t1,0,0,0,0,0,1");

		assertEquals(null, re.firstTime());

		re.setDateExpression("\t\t0,0,1,1,1,0,0");

		assertEquals(df.parse("2017-06-13 23:00", new ParsePosition(0)), re.firstTime());

	}

	@Test
	public void testLastTime() {
		RepeatEntry re = new RepeatEntry();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		re.setStartDate(df.parse("2017-06-12 00:00", new ParsePosition(0)));
		re.setStopDate(df.parse("2017-06-15 00:00", new ParsePosition(0)));
		re.setFromTime(1380); // 23:00
		re.setToTime(1410); // 23:30
		re.setDateExpression("\t\t1,0,0,0,0,0,1");

		assertEquals(null, re.lastTime());

		re.setDateExpression("\t\t0,0,1,1,1,0,0");

		assertEquals(df.parse("2017-06-15 23:00", new ParsePosition(0)), re.lastTime());

	}
}

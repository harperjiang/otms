package org.harper.otms.calendar.entity;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.junit.Test;

public class RepeatEntryTest extends RepeatEntry {

	@Test
	public void testConvert() {
		RepeatEntry re = new RepeatEntry();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		re.setStartDate(df.parse("2015-07-19", new ParsePosition(0)));
		re.setStopDate(df.parse("2015-07-31", new ParsePosition(0)));
		re.setStartTime(840); // 14:00
		re.setStopTime(960); // 16:00

		re.convert(TimeZone.getTimeZone("US/Eastern"),
				TimeZone.getTimeZone("UTC"));

		assertEquals(1080, re.getStartTime());
		assertEquals(1200, re.getStopTime());
		assertEquals(df.parse("2015-07-19", new ParsePosition(0)),
				re.getStartDate());
		assertEquals(df.parse("2015-07-31", new ParsePosition(0)),
				re.getStopDate());
	}

}

package org.harper.otms.lesson.entity;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.harper.otms.lesson.entity.OneoffEntry;
import org.junit.Assert;
import org.junit.Test;

public class OneoffEntryTest extends OneoffEntry {

	@Test
	public void testConvert() {
		OneoffEntry entry = new OneoffEntry();

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		entry.setFromTime(format
				.parse("2015-06-01 03:00", new ParsePosition(0)));
		entry.setToTime(format.parse("2015-06-01 07:00", new ParsePosition(0)));

		entry.convert(TimeZone.getTimeZone("US/Eastern"),
				TimeZone.getTimeZone("US/Central"));

		Date expFrom = format.parse("2015-06-01 02:00", new ParsePosition(0));
		Date expTo = format.parse("2015-06-01 06:00", new ParsePosition(0));

		Assert.assertEquals(expFrom, entry.getFromTime());
		Assert.assertEquals(expTo, entry.getToTime());
	}
}

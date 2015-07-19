package org.harper.otms.calendar.entity;

import java.util.Date;
import java.util.TimeZone;

import org.harper.otms.calendar.service.util.DateUtil;
import org.junit.Test;
import org.junit.Assert;

public class OneoffEntryTest extends OneoffEntry {

	@Test
	public void testConvert() {
		OneoffEntry entry = new OneoffEntry();

		Date current = DateUtil.truncate(new Date());
		entry.setDate(current);
		entry.setStartTime(510);
		entry.setStopTime(550);

		entry.convert(TimeZone.getTimeZone("US/Eastern"),
				TimeZone.getTimeZone("US/Central"));

		Assert.assertEquals(current, entry.getDate());
		Assert.assertEquals(450, entry.getStartTime());
		Assert.assertEquals(490, entry.getStopTime());
	}

}

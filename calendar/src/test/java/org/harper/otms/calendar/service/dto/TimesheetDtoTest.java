package org.harper.otms.calendar.service.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.harper.otms.auth.entity.User;
import org.harper.otms.calendar.entity.Timesheet;
import org.junit.Test;

public class TimesheetDtoTest {

	@Test
	public void testFromToPos() throws Exception {
		Timesheet ts = new Timesheet();
		TimesheetDto tsdto = new TimesheetDto();

		User user = new User();
		user.setTimezone(TimeZone.getTimeZone("Asia/Shanghai"));

		StringBuilder builder = new StringBuilder();
		builder.append(Timesheet.DEFAULT);

		for (int i = 0; i < 7; i++) {
			// every day at 5:30
			builder.setCharAt(i * 48 + 10, '1');
		}

		tsdto.setDefaultValue(builder.toString());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		StringBuilder sb1 = new StringBuilder();
		sb1.append(Timesheet.DEFAULT);
		sb1.setCharAt(0, '2');
		sb1.setCharAt(6 * 48 + 47, '2');

		StringBuilder sb2 = new StringBuilder();
		sb2.append(Timesheet.DEFAULT);
		sb2.setCharAt(0, '3');
		sb2.setCharAt(6 * 48 + 47, '3');

		StringBuilder sb3 = new StringBuilder();
		sb3.append(Timesheet.DEFAULT);
		sb3.setCharAt(0, '4');
		sb3.setCharAt(6 * 48 + 47, '4');

		tsdto.setRefDates(new Date[] { sdf.parse("2017-06-25"), sdf.parse("2017-07-02"), sdf.parse("2017-07-23") });
		tsdto.setValues(new String[] { sb1.toString(), sb2.toString(), sb3.toString() });
		tsdto.to(ts, user);

		Map<Date, String> refdata = new HashMap<Date, String>();
		for (int i = 0; i < 3; i++) {
			refdata.put(tsdto.getRefDates()[i], tsdto.getValues()[i]);
		}

		StringBuilder b2 = new StringBuilder();
		b2.append(Timesheet.DEFAULT);

		for (int i = 0; i < 7; i++) {
			// every day at 10:30
			b2.setCharAt(i * 48 + +42, '1');
		}
		assertEquals(b2.toString(), ts.getDefaultValue());

		assertEquals(5, ts.getValues().size());
		List<Date> dates = new ArrayList<Date>(ts.getValues().keySet());
		Collections.sort(dates);

		assertEquals(sdf.parse("2017-06-18"), dates.get(0));
		assertEquals(sdf.parse("2017-06-25"), dates.get(1));
		assertEquals(sdf.parse("2017-07-02"), dates.get(2));
		assertEquals(sdf.parse("2017-07-16"), dates.get(3));
		assertEquals(sdf.parse("2017-07-23"), dates.get(4));

		assertEquals(builder.substring(16) + sb1.substring(0, 16), ts.getValues().get(dates.get(0)));
		assertEquals(sb1.substring(16) + sb2.substring(0, 16), ts.getValues().get(dates.get(1)));
		assertEquals(sb2.substring(16) + builder.substring(0, 16), ts.getValues().get(dates.get(2)));
		assertEquals(builder.substring(16) + sb3.substring(0, 16), ts.getValues().get(dates.get(3)));
		assertEquals(sb3.substring(16) + builder.substring(0, 16), ts.getValues().get(dates.get(4)));

		TimesheetDto tsdto2 = new TimesheetDto();

		tsdto2.from(ts, user);

		assertEquals(builder.toString(), tsdto.getDefaultValue());
		assertEquals(3, tsdto2.getRefDates().length);
		assertEquals(3, tsdto2.getValues().length);

		for (int i = 0; i < 3; i++) {
			assertEquals(refdata.get(tsdto2.getRefDates()[i]), tsdto2.getValues()[i]);
		}

	}

	@Test
	public void testFromToNeg() throws Exception {
		Timesheet ts = new Timesheet();
		TimesheetDto tsdto = new TimesheetDto();

		User user = new User();
		user.setTimezone(TimeZone.getTimeZone("America/Chicago"));

		StringBuilder builder = new StringBuilder();
		builder.append(Timesheet.DEFAULT);

		for (int i = 0; i < 7; i++) {
			// every day at 5:30
			builder.setCharAt(i * 48 + 10, '1');
		}

		tsdto.setDefaultValue(builder.toString());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		StringBuilder sb1 = new StringBuilder();
		sb1.append(Timesheet.DEFAULT);
		sb1.setCharAt(0, '2');
		sb1.setCharAt(6 * 48 + 47, '2');

		StringBuilder sb2 = new StringBuilder();
		sb2.append(Timesheet.DEFAULT);
		sb2.setCharAt(0, '3');
		sb2.setCharAt(6 * 48 + 47, '3');

		StringBuilder sb3 = new StringBuilder();
		sb3.append(Timesheet.DEFAULT);
		sb3.setCharAt(0, '4');
		sb3.setCharAt(6 * 48 + 47, '4');

		tsdto.setRefDates(new Date[] { sdf.parse("2017-06-25"), sdf.parse("2017-07-02"), sdf.parse("2017-07-23") });
		tsdto.setValues(new String[] { sb1.toString(), sb2.toString(), sb3.toString() });
		tsdto.to(ts, user);

		Map<Date, String> refdata = new HashMap<Date, String>();
		for (int i = 0; i < 3; i++) {
			refdata.put(tsdto.getRefDates()[i], tsdto.getValues()[i]);
		}

		StringBuilder b2 = new StringBuilder();
		b2.append(Timesheet.DEFAULT);

		for (int i = 0; i < 7; i++) {
			// every day at 10:30
			b2.setCharAt(i * 48 + 20, '1');
		}
		assertEquals(b2.toString(), ts.getDefaultValue());

		assertEquals(5, ts.getValues().size());
		List<Date> dates = new ArrayList<Date>(ts.getValues().keySet());
		Collections.sort(dates);

		assertEquals(sdf.parse("2017-06-25"), dates.get(0));
		assertEquals(sdf.parse("2017-07-02"), dates.get(1));
		assertEquals(sdf.parse("2017-07-09"), dates.get(2));
		assertEquals(sdf.parse("2017-07-23"), dates.get(3));
		assertEquals(sdf.parse("2017-07-30"), dates.get(4));

		int len = TimesheetDto.LENGTH;
		assertEquals(builder.substring(len - 10) + sb1.substring(0, len - 10), ts.getValues().get(dates.get(0)));
		assertEquals(sb1.substring(len - 10) + sb2.substring(0, len - 10), ts.getValues().get(dates.get(1)));
		assertEquals(sb2.substring(len - 10) + builder.substring(0, len - 10), ts.getValues().get(dates.get(2)));
		assertEquals(builder.substring(len - 10) + sb3.substring(0, len - 10), ts.getValues().get(dates.get(3)));
		assertEquals(sb3.substring(len - 10) + builder.substring(0, len - 10), ts.getValues().get(dates.get(4)));

		TimesheetDto tsdto2 = new TimesheetDto();

		tsdto2.from(ts, user);

		assertEquals(builder.toString(), tsdto.getDefaultValue());
		assertEquals(3, tsdto2.getRefDates().length);
		assertEquals(3, tsdto2.getValues().length);

		for (int i = 0; i < 3; i++) {
			assertEquals(refdata.get(tsdto2.getRefDates()[i]), tsdto2.getValues()[i]);
		}
	}

	@Test
	public void testShift() {
		fail("Not yet implemented");
	}

	@Test
	public void testShiftRight() {
		fail("Not yet implemented");
	}

	@Test
	public void testShiftLeft() {
		fail("Not yet implemented");
	}

}

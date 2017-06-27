package org.harper.otms.calendar.service.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import org.harper.otms.auth.entity.User;
import org.harper.otms.calendar.entity.Timesheet;
import org.harper.otms.calendar.service.util.DateUtil;

public class TimesheetDto {

	public static final int LENGTH = 7 * 48;

	private String defaultValue;

	private Date[] refDates = new Date[0];

	private String[] values = new String[0];

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Date[] getRefDates() {
		return refDates;
	}

	public void setRefDates(Date[] refDates) {
		this.refDates = refDates;
	}

	public String[] getValues() {
		return values;
	}

	public void setValues(String[] values) {
		this.values = values;
	}

	public void from(Timesheet timesheet, User viewer) {
		int offset = viewer.getTimezone().getOffset(System.currentTimeMillis()) / 1800000;

		// Convert the default timesheet
		setDefaultValue(shift(timesheet.getDefaultValue(), -offset));

		// Convert the per-month timesheet
		Map<Date, String> data = new HashMap<Date, String>();
		data.putAll(timesheet.getValues());

		// Shift the value
		if (offset < 0) {
			shiftLeft(data, timesheet.getDefaultValue(), -offset);
		} else {
			shiftRight(data, timesheet.getDefaultValue(), offset);
		}

		// Remove the entries equal to default
		Iterator<Entry<Date, String>> ite = data.entrySet().iterator();
		while (ite.hasNext()) {
			Entry<Date, String> entry = ite.next();
			if (entry.getValue().equals(getDefaultValue())) {
				ite.remove();
			}
		}

		Date[] dates = new Date[data.size()];
		String[] values = new String[data.size()];
		int counter = 0;
		for (Entry<Date, String> entry : data.entrySet()) {
			dates[counter] = entry.getKey();
			values[counter++] = entry.getValue();
		}

		setRefDates(dates);
		setValues(values);
	}

	public void to(Timesheet timesheet, User owner) {
		// offset count in 30 min
		int offset = owner.getTimezone().getOffset(System.currentTimeMillis()) / 1800000;

		// Convert the default timesheet
		timesheet.setDefaultValue(shift(defaultValue, offset));

		// Convert the per-month timesheet
		Map<Date, String> data = new HashMap<Date, String>();
		for (int i = 0; i < getRefDates().length; i++) {
			data.put(getRefDates()[i], getValues()[i]);
		}

		// Shift the value
		if (offset < 0) {
			shiftRight(data, defaultValue, -offset);
		} else {
			shiftLeft(data, defaultValue, offset);
		}

		// Remove the entries equal to default
		Iterator<Entry<Date, String>> ite = data.entrySet().iterator();
		while (ite.hasNext()) {
			Entry<Date, String> entry = ite.next();
			if (entry.getValue().equals(timesheet.getDefaultValue())) {
				ite.remove();
			}
		}
		timesheet.getValues().clear();
		timesheet.getValues().putAll(data);
	}

	protected String shift(String input, int offset) {
		StringBuilder sbuilder = new StringBuilder();
		if (offset > 0) {
			sbuilder.append(input.substring(offset));
			sbuilder.append(input.substring(0, offset));
		} else {
			sbuilder.append(input.substring(LENGTH + offset));
			sbuilder.append(input.substring(0, LENGTH + offset));
		}
		return sbuilder.toString();
	}

	protected void shiftRight(Map<Date, String> data, String defaultValue, int offset) {
		if (data.isEmpty())
			return;

		Map<Date, String> newval = new HashMap<Date, String>();
		// From small to big
		PriorityQueue<Date> pq = new PriorityQueue<>();
		pq.addAll(data.keySet());

		StringBuilder currentSb = new StringBuilder();
		StringBuilder nextSb = new StringBuilder();

		while (!pq.isEmpty()) {
			currentSb.delete(0, currentSb.length());
			nextSb.delete(0, nextSb.length());

			Date current = pq.poll();
			String currentVal = data.get(current);
			Date prevWeek = DateUtil.offset(current, -7);
			String prevVal = data.getOrDefault(prevWeek, defaultValue);
			currentSb.append(prevVal.substring(LENGTH - offset));
			currentSb.append(currentVal.substring(0, LENGTH - offset));
			newval.put(current, currentSb.toString());
			Date nextWeek = DateUtil.offset(current, 7);

			if (!data.containsKey(nextWeek)) {
				nextSb.append(currentVal.substring(LENGTH - offset));
				nextSb.append(defaultValue.substring(0, LENGTH - offset));
				newval.put(nextWeek, nextSb.toString());
			}

		}
		data.putAll(newval);

	}

	protected void shiftLeft(Map<Date, String> data, String defaultValue, int offset) {
		if (data.isEmpty())
			return;
		Map<Date, String> newval = new HashMap<Date, String>();
		// From big to small
		PriorityQueue<Date> pq = new PriorityQueue<>((Date a, Date b) -> -a.compareTo(b));
		pq.addAll(data.keySet());

		StringBuilder currentbuilder = new StringBuilder();
		StringBuilder prevbuilder = new StringBuilder();

		while (!pq.isEmpty()) {
			currentbuilder.delete(0, currentbuilder.length());
			prevbuilder.delete(0, prevbuilder.length());

			Date current = pq.poll();
			String currentVal = data.get(current);
			Date nextWeek = DateUtil.offset(current, 7);
			String nextVal = data.getOrDefault(nextWeek, defaultValue);
			currentbuilder.append(currentVal.substring(offset));
			currentbuilder.append(nextVal.substring(0, offset));
			newval.put(current, currentbuilder.toString());

			Date prevWeek = DateUtil.offset(current, -7);
			if (!data.containsKey(prevWeek)) {
				prevbuilder.append(defaultValue.substring(offset));
				prevbuilder.append(currentVal.substring(0, offset));
				newval.put(prevWeek, prevbuilder.toString());
			}
		}
		data.putAll(newval);
	}
}

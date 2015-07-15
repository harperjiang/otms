package org.harper.otms.calendar.service.util;

import java.text.MessageFormat;

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
}

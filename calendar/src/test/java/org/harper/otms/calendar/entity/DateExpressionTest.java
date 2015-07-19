package org.harper.otms.calendar.entity;

import org.harper.otms.calendar.entity.RepeatEntry.DateExpression;
import org.junit.Assert;
import org.junit.Test;

public class DateExpressionTest {

	@Test
	public void testToString() {
		DateExpression de = new DateExpression("\t\t0,0,0,0,0,0,1");
		de.shift(-4);
		Assert.assertEquals("\t\t0,0,1,0,0,0,0", de.toString());
	}
}

package org.harper.otms.calendar.service.util;

import static org.junit.Assert.*;

import org.harper.otms.auth.service.util.PasswordUtil;
import org.junit.Test;

public class PasswordUtilTest {

	@Test
	public void testDigest() {
		assertEquals("4b79e0e51941cc9cd5982e211ee7a78c", PasswordUtil.digest("debbie@1984"));
	}

}

package org.harper.otms.auth.service.util;

import org.springframework.util.DigestUtils;

public class PasswordUtil {

	public static String digest(String input) {
		try {
			byte[] digestBytes = DigestUtils.md5Digest(input.getBytes("utf8"));
			StringBuilder result = new StringBuilder();
			for (byte b : digestBytes) {
				int val = b;
				if (val < 0)
					val += 255;
				result.append(Integer.toHexString(val));
			}
			return result.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

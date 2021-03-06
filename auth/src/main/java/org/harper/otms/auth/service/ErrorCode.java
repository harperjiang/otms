package org.harper.otms.auth.service;

public interface ErrorCode {

	int USER = 100;

	int USER_EXIST_ID = USER + 1;

	int USER_EXIST_EMAIL = USER + 2;

	int USER_FAIL_LOGIN = USER + 3;

	int USER_NOT_ACTIVATED = USER + 4;

	int USER_ALREADY_ACTIVATED = USER + 5;

	int USER_UNKNOWN_SOURCE = USER + 6;

	int USER_NOT_LINKED = USER + 7;

	int USER_LINK_FAILED = USER + 8;

	int SESSION = 200;

	int SESSION_NOTLOGIN = SESSION + 2;

	int SESSION_EXPIRED = SESSION + 1;

	int SYSTEM = 300;

	int SYSTEM_INVALID_PARAM = SYSTEM + 1;

	int SYSTEM_CAPTCHA_FAIL = SYSTEM + 2;

	int SYSTEM_NO_USER = SYSTEM + 3;

	int SYSTEM_NO_AUTH = SYSTEM + 4;

	int SYSTEM_DATA_NOT_FOUND = SYSTEM + 5;
}

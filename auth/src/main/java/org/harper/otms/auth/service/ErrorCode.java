package org.harper.otms.auth.service;

public interface ErrorCode {

	int USER = 100;

	int USER_EXIST_ID = USER + 1;

	int USER_EXIST_EMAIL = USER + 2;

	int USER_NAME_NOTEXIST = USER + 3;

	int SESSION = 200;

	int SESSION_NOTLOGIN = SESSION + 2;

	int SESSION_EXPIRED = SESSION + 1;
}

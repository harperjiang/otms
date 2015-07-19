package org.harper.otms.calendar.service;

public interface ErrorCode {

	int CALENDAR_SERVICE = 100;

	int TUTOR_NOT_FOUND = CALENDAR_SERVICE + 1;

	int USER = 200;

	int USER_EXIST_ID = USER + 1;

	int USER_EXIST_EMAIL = USER + 2;

	int DATA = 400;

	int DATA_NOT_FOUND = DATA + 1;

	int SYS = 500;

	int SYS_NO_USER = SYS + 1;

	int SYS_NO_AUTH = SYS + 2;

}

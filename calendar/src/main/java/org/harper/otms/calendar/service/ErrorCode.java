package org.harper.otms.calendar.service;

public interface ErrorCode {

	int CALENDAR_SERVICE = 1100;

	int TUTOR_NOT_FOUND = CALENDAR_SERVICE + 1;

	int DATA = 1400;

	int DATA_NOT_FOUND = DATA + 1;

	int SYS = 1500;

	int SYS_NO_USER = SYS + 1;

	int SYS_NO_AUTH = SYS + 2;

}

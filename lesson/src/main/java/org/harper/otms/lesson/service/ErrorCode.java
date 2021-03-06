package org.harper.otms.lesson.service;

public interface ErrorCode extends org.harper.otms.auth.service.ErrorCode {

	int CALENDAR_SERVICE = 1100;

	int TUTOR_NOT_FOUND = CALENDAR_SERVICE + 1;

	int LESSON_SERVICE = 1200;

	int LESSON_ITEM_EXPIRED = LESSON_SERVICE + 1;

	// At least one lesson item has been generated and lesson is non-changable.
	int LESSON_IN_PROGRESS = LESSON_SERVICE + 2;

	// Invalid lesson id or so
	int LESSON_INVALID = LESSON_SERVICE + 3;

	int LESSON_IN_PAST = LESSON_SERVICE + 4;

	int LESSON_INVALID_SCHEDULE = LESSON_SERVICE + 5;
	
	int LESSON_ITEM_HAS_FEEDBACK = LESSON_SERVICE + 6;
}

package org.harper.otms.calendar.service;

import org.harper.otms.calendar.service.dto.AdminScheduleLessonDto;
import org.harper.otms.calendar.service.dto.AdminScheduleLessonResponseDto;
import org.harper.otms.calendar.service.dto.AdminSearchLessonDto;
import org.harper.otms.calendar.service.dto.AdminSearchLessonResponseDto;
import org.harper.otms.calendar.service.dto.AdminUpdateAccountDto;
import org.harper.otms.calendar.service.dto.AdminUpdateAccountResponseDto;
import org.harper.otms.calendar.service.dto.AdminUpdateLessonDto;
import org.harper.otms.calendar.service.dto.AdminUpdateLessonResponseDto;

public interface AdminService {

	/**
	 * Directly schedule a valid lesson
	 * 
	 * @param request
	 * @return
	 */
	AdminScheduleLessonResponseDto scheduleLesson(AdminScheduleLessonDto request);

	/**
	 * Search all lessons within a time range. Will return all occurrence of
	 * lesson items
	 * 
	 * @param request
	 * @return
	 */
	AdminSearchLessonResponseDto searchLesson(AdminSearchLessonDto request);

	/**
	 * Update Lesson / LessonItem time or cancel lesson
	 * 
	 * @param request
	 * @return
	 */
	AdminUpdateLessonResponseDto updateLesson(AdminUpdateLessonDto request);

	/**
	 * Update user account information
	 * 
	 * @param request
	 * @return
	 */
	AdminUpdateAccountResponseDto updateAccount(AdminUpdateAccountDto request);

}

package org.harper.otms.calendar.service;

import org.harper.otms.calendar.service.dto.CancelLessonDto;
import org.harper.otms.calendar.service.dto.ChangeLessonStatusDto;
import org.harper.otms.calendar.service.dto.ChangeLessonStatusResponseDto;
import org.harper.otms.calendar.service.dto.ConfirmCancelDto;
import org.harper.otms.calendar.service.dto.GetLessonDto;
import org.harper.otms.calendar.service.dto.GetLessonItemDto;
import org.harper.otms.calendar.service.dto.GetLessonItemResponseDto;
import org.harper.otms.calendar.service.dto.GetLessonResponseDto;
import org.harper.otms.calendar.service.dto.GetRequestedLessonDto;
import org.harper.otms.calendar.service.dto.GetRequestedLessonResponseDto;
import org.harper.otms.calendar.service.dto.GetSnapshotDto;
import org.harper.otms.calendar.service.dto.GetSnapshotResponseDto;
import org.harper.otms.calendar.service.dto.MakeLessonItemDto;
import org.harper.otms.calendar.service.dto.MakeLessonItemResponseDto;
import org.harper.otms.calendar.service.dto.SetupLessonDto;
import org.harper.otms.calendar.service.dto.SetupLessonResponseDto;

public interface LessonService {

	/**
	 * Retrieve a lesson
	 * 
	 * @param request
	 * @return
	 */
	GetLessonResponseDto getLesson(GetLessonDto request);

	/**
	 * Retrieve lesson item
	 * 
	 * @param request
	 * @return
	 */
	GetLessonItemResponseDto getLessonItem(GetLessonItemDto request);

	/**
	 * Retrieve snapshot
	 * 
	 * @param request
	 * @return
	 */
	GetSnapshotResponseDto getSnapshot(GetSnapshotDto request);

	/**
	 * Make a lesson item from its lesson
	 * 
	 * @param request
	 * @return
	 */
	MakeLessonItemResponseDto makeLessonItem(MakeLessonItemDto request);

	/**
	 * Client setup meeting with tutor
	 * 
	 * @param request
	 */
	SetupLessonResponseDto setupLesson(SetupLessonDto request);

	/**
	 * 
	 * @param request
	 * @return
	 */
	GetRequestedLessonResponseDto getRequestedLessons(
			GetRequestedLessonDto request);
	
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	ChangeLessonStatusResponseDto changeLessonStatus(ChangeLessonStatusDto request);
	
	/**
	 * Both client and tutor can propose the cancellation of a meeting. Client
	 * can directly cancel a meeting Tutor always need to get approval from
	 * client to cancel a meeting
	 * 
	 * @param request
	 */
	void cancelLesson(CancelLessonDto request);

	/**
	 * Client confirm tutor's request of canceling a meeting or a item
	 * 
	 * @param request
	 */
	void confirmCancel(ConfirmCancelDto request);

	/**
	 * Clear finished calendar item and mark it as snapshots
	 */
	void updateSnapshot();

}

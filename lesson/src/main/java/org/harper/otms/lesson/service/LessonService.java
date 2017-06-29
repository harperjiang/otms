package org.harper.otms.lesson.service;

import org.harper.otms.lesson.service.dto.CancelLessonItemDto;
import org.harper.otms.lesson.service.dto.CancelLessonItemResponseDto;
import org.harper.otms.lesson.service.dto.ChangeLessonStatusDto;
import org.harper.otms.lesson.service.dto.ChangeLessonStatusResponseDto;
import org.harper.otms.lesson.service.dto.GetLessonDto;
import org.harper.otms.lesson.service.dto.GetLessonHistoryDto;
import org.harper.otms.lesson.service.dto.GetLessonHistoryResponseDto;
import org.harper.otms.lesson.service.dto.GetLessonItemDto;
import org.harper.otms.lesson.service.dto.GetLessonItemResponseDto;
import org.harper.otms.lesson.service.dto.GetLessonResponseDto;
import org.harper.otms.lesson.service.dto.GetOngoingLessonDto;
import org.harper.otms.lesson.service.dto.GetOngoingLessonResponseDto;
import org.harper.otms.lesson.service.dto.GetRequestedLessonDto;
import org.harper.otms.lesson.service.dto.GetRequestedLessonResponseDto;
import org.harper.otms.lesson.service.dto.MakeLessonItemDto;
import org.harper.otms.lesson.service.dto.MakeLessonItemResponseDto;
import org.harper.otms.lesson.service.dto.SetupLessonDto;
import org.harper.otms.lesson.service.dto.SetupLessonResponseDto;
import org.harper.otms.lesson.service.dto.TriggerLessonDto;
import org.harper.otms.lesson.service.dto.TriggerLessonResponseDto;

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
	 * Make or modify a lesson item from its lesson
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
	GetRequestedLessonResponseDto getRequestedLessons(GetRequestedLessonDto request);

	/**
	 * 
	 * @param request
	 * @return
	 */
	GetOngoingLessonResponseDto getOngoingLessons(GetOngoingLessonDto request);

	/**
	 * 
	 * @param request
	 * @return
	 */
	GetLessonHistoryResponseDto getLessonHistory(GetLessonHistoryDto request);

	/**
	 * 
	 * @param request
	 * @return
	 */
	ChangeLessonStatusResponseDto changeLessonStatus(ChangeLessonStatusDto request);

	/**
	 * Both client and tutor can propose the cancellation of a lesson item
	 * before it occurs
	 * 
	 * @param request
	 * @return
	 */
	CancelLessonItemResponseDto cancelLessonItem(CancelLessonItemDto request);

	/**
	 * Trigger a given lesson at specific time. This is generally achieved via a
	 * scheduler service and is not intended to be invoked by user
	 */
	TriggerLessonResponseDto triggerLesson(TriggerLessonDto request);

}

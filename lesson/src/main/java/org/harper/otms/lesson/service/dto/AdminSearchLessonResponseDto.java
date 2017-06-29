package org.harper.otms.lesson.service.dto;

import java.util.ArrayList;
import java.util.List;

import org.harper.otms.common.dto.ResponseDto;

public class AdminSearchLessonResponseDto extends ResponseDto {

	List<EventDto> events = new ArrayList<EventDto>();

	public List<EventDto> getEvents() {
		return events;
	}

	public void setEvents(List<EventDto> events) {
		this.events = events;
	}

}

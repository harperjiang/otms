package org.harper.otms.lesson.service.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.harper.otms.common.dto.ResponseDto;

public class GetCalendarEventResponseDto extends ResponseDto {

	private List<EventDto> events = new ArrayList<EventDto>();

	public void addEvent(EventDto event) {
		events.add(event);
	}

	public void convert(TimeZone from, TimeZone to) {
		for (EventDto event : events) {
			event.convert(from, to);
		}
	}

	public List<EventDto> getEvents() {
		return events;
	}

}

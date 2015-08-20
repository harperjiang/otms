package org.harper.otms.calendar.service;

import org.harper.otms.calendar.service.dto.GetTodoDto;
import org.harper.otms.calendar.service.dto.GetTodoResponseDto;

public interface TodoService {
	
	public GetTodoResponseDto getTodos(GetTodoDto request);
	
}

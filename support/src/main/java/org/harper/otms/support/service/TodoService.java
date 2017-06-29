package org.harper.otms.support.service;

import org.harper.otms.support.service.dto.GetTodoDto;
import org.harper.otms.support.service.dto.GetTodoResponseDto;

public interface TodoService {
	
	public GetTodoResponseDto getTodos(GetTodoDto request);
	
}

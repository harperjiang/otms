package org.harper.otms.support.service.dto;

import java.util.List;

import org.harper.otms.common.dto.ResponseDto;

public class GetTodoResponseDto extends ResponseDto {

	private List<TodoDto> todos;

	public List<TodoDto> getTodos() {
		return todos;
	}

	public void setTodos(List<TodoDto> todos) {
		this.todos = todos;
	}

}

package org.harper.otms.calendar.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.harper.otms.auth.dao.UserDao;
import org.harper.otms.auth.entity.User;
import org.harper.otms.calendar.dao.TodoDao;
import org.harper.otms.calendar.entity.Todo;
import org.harper.otms.calendar.service.TodoService;
import org.harper.otms.calendar.service.dto.GetTodoDto;
import org.harper.otms.calendar.service.dto.GetTodoResponseDto;
import org.harper.otms.calendar.service.dto.TodoDto;

public class DefaultTodoService implements TodoService {

	@Override
	public GetTodoResponseDto getTodos(GetTodoDto request) {
		User owner = getUserDao().findById(request.getCurrentUser());
		List<Todo> todos = getTodoDao().findByOwner(owner);

		List<TodoDto> dtos = new ArrayList<TodoDto>();
		for (Todo todo : todos) {
			TodoDto dto = new TodoDto();
			dto.from(todo);
			dtos.add(dto);
		}
		GetTodoResponseDto resp = new GetTodoResponseDto();
		resp.setTodos(dtos);
		return resp;
	}

	private UserDao userDao;

	private TodoDao todoDao;

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public TodoDao getTodoDao() {
		return todoDao;
	}

	public void setTodoDao(TodoDao todoDao) {
		this.todoDao = todoDao;
	}

}

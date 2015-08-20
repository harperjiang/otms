package org.harper.otms.calendar.dao;

import java.util.List;

import org.harper.otms.auth.entity.User;
import org.harper.otms.calendar.entity.Todo;
import org.harper.otms.common.dao.Dao;

public interface TodoDao extends Dao<Todo> {
	public List<Todo> findByOwner(User owner);
}

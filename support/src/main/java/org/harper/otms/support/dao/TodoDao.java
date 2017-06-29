package org.harper.otms.support.dao;

import java.util.List;

import org.harper.otms.auth.entity.User;
import org.harper.otms.common.dao.Dao;
import org.harper.otms.support.entity.Todo;
import org.harper.otms.support.entity.Todo.Type;

public interface TodoDao extends Dao<Todo> {
	
	public List<Todo> findByOwner(User owner);

	public Todo findByOwnerTypeAndRefId(User user, Type type, int refid);
}

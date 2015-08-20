package org.harper.otms.calendar.dao.jpa;

import java.util.List;

import org.harper.otms.auth.entity.User;
import org.harper.otms.calendar.dao.TodoDao;
import org.harper.otms.calendar.entity.Todo;
import org.harper.otms.common.dao.JpaDao;

public class JpaTodoDao extends JpaDao<Todo> implements TodoDao {

	@Override
	public List<Todo> findByOwner(User owner) {
		String sql = "select t from Todo t where t.owner = :owner order by t.expireTime";

		return getEntityManager().createQuery(sql, Todo.class)
				.setParameter("owner", owner).getResultList();
	}

}

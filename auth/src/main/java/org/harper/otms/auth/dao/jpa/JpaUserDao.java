package org.harper.otms.auth.dao.jpa;

import org.harper.otms.auth.dao.UserDao;
import org.harper.otms.auth.entity.User;
import org.harper.otms.common.dao.JpaDao;

public class JpaUserDao extends JpaDao<User> implements UserDao {

	@Override
	public User findByName(String username) {
		String sql = "select u from User u where u.name = :username";
		return getSingleResult(getEntityManager().createQuery(sql, User.class)
				.setParameter("username", username));
	}

	@Override
	public User findByEmail(String email) {
		String sql = "select u from User u where u.email = :email";
		return getSingleResult(getEntityManager().createQuery(sql, User.class)
				.setParameter("email", email));
	}

}

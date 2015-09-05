package org.harper.otms.auth.dao.jpa;

import org.harper.otms.auth.dao.UserDao;
import org.harper.otms.auth.entity.User;
import org.harper.otms.auth.external.ExternalSystem;
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

	@Override
	public User findByActivateCode(String uuid) {
		String sql = "select u from User u where u.activationCode = :act and u.activated = false";
		return getSingleResult(getEntityManager().createQuery(sql, User.class)
				.setParameter("act", uuid));
	}

	@Override
	public User findBySource(ExternalSystem system, String id) {
		String sql = "select u from User u where u.sourceSystem = :sys and u.sourceId = :id";
		return getSingleResult(getEntityManager().createQuery(sql, User.class)
				.setParameter("sys", system).setParameter("id", id));
	}

}

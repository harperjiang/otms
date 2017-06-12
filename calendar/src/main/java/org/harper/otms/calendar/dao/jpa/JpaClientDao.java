package org.harper.otms.calendar.dao.jpa;

import javax.persistence.NoResultException;

import org.harper.otms.calendar.dao.ClientDao;
import org.harper.otms.calendar.entity.Client;
import org.harper.otms.common.dao.JpaDao;

public class JpaClientDao extends JpaDao<Client> implements ClientDao {

	@Override
	public Client findByName(String name) {
		String sql = "select c from Client c where c.name = :name";
		try {
			return getEntityManager().createQuery(sql, Client.class).setParameter("name", name).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}

}

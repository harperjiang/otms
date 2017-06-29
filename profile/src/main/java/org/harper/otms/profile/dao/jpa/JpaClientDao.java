package org.harper.otms.profile.dao.jpa;

import javax.persistence.NoResultException;

import org.harper.otms.common.dao.JpaDao;
import org.harper.otms.profile.dao.ClientDao;
import org.harper.otms.profile.entity.Client;

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

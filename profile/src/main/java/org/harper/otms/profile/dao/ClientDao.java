package org.harper.otms.profile.dao;

import org.harper.otms.common.dao.Dao;
import org.harper.otms.profile.entity.Client;

public interface ClientDao extends Dao<Client> {

	public Client findByName(String name);
}

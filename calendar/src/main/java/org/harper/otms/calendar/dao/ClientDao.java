package org.harper.otms.calendar.dao;

import org.harper.otms.calendar.entity.Client;
import org.harper.otms.common.dao.Dao;

public interface ClientDao extends Dao<Client> {

	public Client findByName(String name);
}

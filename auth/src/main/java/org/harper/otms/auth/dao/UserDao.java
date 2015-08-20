package org.harper.otms.auth.dao;

import org.harper.otms.auth.entity.User;
import org.harper.otms.common.dao.Dao;

public interface UserDao extends Dao<User> {

	User findByName(String username);

	User findByEmail(String email);

	User findByActivateCode(String uuid);

}

package org.harper.otms.profile.dao;

import java.util.List;

import org.harper.otms.common.dao.Dao;
import org.harper.otms.profile.entity.Tutor;

public interface TutorDao extends Dao<Tutor> {

	Tutor findByName(String tutorName);

	List<Tutor> findPopular();

}

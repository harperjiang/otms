package org.harper.otms.calendar.dao;

import java.util.List;

import org.harper.otms.calendar.entity.Tutor;
import org.harper.otms.common.dao.Dao;

public interface TutorDao extends Dao<Tutor> {

	Tutor findByName(String tutorName);

	List<Tutor> findPopular();

}

package org.harper.otms.calendar.dao;

import java.util.Date;
import java.util.List;

import org.harper.otms.auth.entity.User;
import org.harper.otms.calendar.entity.LessonItem;
import org.harper.otms.common.dao.Dao;

public interface LessonItemDao extends Dao<LessonItem> {

	List<LessonItem> findWithin(User user, Date fromDate, Date toDate);

}

package org.harper.otms.calendar.dao;

import java.util.Date;
import java.util.List;

import org.harper.otms.auth.entity.User;
import org.harper.otms.calendar.entity.LessonItem;
import org.harper.otms.calendar.entity.LessonItem.Status;
import org.harper.otms.common.dao.Dao;
import org.harper.otms.common.dto.PagingDto;

public interface LessonItemDao extends Dao<LessonItem> {

	public List<LessonItem> findWithin(User user, Date fromDate, Date toDate,
			Status status, PagingDto paging);
}

package org.harper.otms.lesson.dao;

import java.util.Date;
import java.util.List;

import org.harper.otms.auth.entity.User;
import org.harper.otms.common.dao.Dao;
import org.harper.otms.common.dto.PagingDto;
import org.harper.otms.lesson.entity.LessonItem;
import org.harper.otms.lesson.entity.LessonItem.Status;
import org.harper.otms.profile.entity.Client;
import org.harper.otms.profile.entity.Tutor;

public interface LessonItemDao extends Dao<LessonItem> {

	public List<LessonItem> findWithin(User user, Date fromDate, Date toDate, Status status, PagingDto paging);

	public List<LessonItem> findWithin(Tutor tutor, Client client, Date fromDate, Date toDate);
}

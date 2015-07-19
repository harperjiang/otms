package org.harper.otms.calendar.dao.jpa;

import java.util.Date;
import java.util.List;

import org.harper.otms.auth.entity.User;
import org.harper.otms.calendar.dao.LessonItemDao;
import org.harper.otms.calendar.entity.LessonItem;
import org.harper.otms.common.dao.JpaDao;

public class JpaLessonItemDao extends JpaDao<LessonItem> implements
		LessonItemDao {

	@Override
	public List<LessonItem> findWithin(User part, Date fromDate, Date toDate) {
		String sql = "select s from LessonItem s where"
				+ " (s.date between :from and :to) and"
				+ ("tutor".equals(part.getType()) ? " s.lesson.tutor.id = :id"
						: " s.lesson.client.id = :id");

		return getEntityManager().createQuery(sql, LessonItem.class)
				.setParameter("from", fromDate).setParameter("to", toDate)
				.setParameter("id", part.getId()).getResultList();
	}

}

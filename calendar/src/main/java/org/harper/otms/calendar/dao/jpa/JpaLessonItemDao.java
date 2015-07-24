package org.harper.otms.calendar.dao.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.harper.otms.auth.entity.User;
import org.harper.otms.calendar.dao.LessonItemDao;
import org.harper.otms.calendar.entity.LessonItem;
import org.harper.otms.calendar.entity.LessonItem.Status;
import org.harper.otms.common.dao.JpaDao;

public class JpaLessonItemDao extends JpaDao<LessonItem> implements
		LessonItemDao {

	@Override
	public List<LessonItem> findWithin(User user, Date fromDate, Date toDate,
			Status status) {
		if (fromDate.after(toDate)) {
			return new ArrayList<LessonItem>();
		}
		StringBuilder sql = new StringBuilder()
				.append("select li from LessonItem li")
				.append(" where li.status = :status")
				.append(" and li.toTime >= :fromDate")
				.append(" and li.fromTime <= :toDate");
		if ("client".equals(user.getType())) {
			sql.append(" and li.lesson.client.id =:id");
		} else {
			sql.append(" and li.lesson.tutor.id =:id");
		}
		return getEntityManager().createQuery(sql.toString(), LessonItem.class)
				.setParameter("status", status)
				.setParameter("fromDate", fromDate)
				.setParameter("toDate", toDate)
				.setParameter("id", user.getId()).getResultList();
	}
}

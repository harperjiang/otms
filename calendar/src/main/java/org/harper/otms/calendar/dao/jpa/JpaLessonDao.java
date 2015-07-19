package org.harper.otms.calendar.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.harper.otms.auth.entity.User;
import org.harper.otms.calendar.dao.LessonDao;
import org.harper.otms.calendar.entity.Lesson;
import org.harper.otms.calendar.entity.Lesson.Status;
import org.harper.otms.calendar.entity.Tutor;
import org.harper.otms.common.dao.JpaDao;

public class JpaLessonDao extends JpaDao<Lesson> implements LessonDao {

	@Override
	@SuppressWarnings("unchecked")
	public List<Lesson> findWithin(User user, Date fromDate, Date toDate,
			Status[] status) {

		StringBuilder oneoffSql = new StringBuilder();

		oneoffSql.append("select l.* from lesson l")
				.append(" join calendar_entry ce")
				.append(" on ce.id = l.calendar").append(" where l.status in ")
				.append(bindingExp(status.length)).append(" and ");
		if ("tutor".equals(user.getType())) {
			oneoffSql.append("l.tutor_id = ?");
		} else {
			oneoffSql.append("l.client_id = ?");
		}

		StringBuilder repeatSql = new StringBuilder(oneoffSql);

		oneoffSql.append(" and ce.calendar_type = 'ONEOFF'");

		repeatSql.append(" and ce.calendar_type = 'REPEAT'");

		oneoffSql.append(" and (ce.oneoff_date between ? and ?)");
		repeatSql.append(" and ce.repeat_start_date <= ?").append(
				" and ce.repeat_stop_date >= ?");

		Query oneoffQuery = getEntityManager().createNativeQuery(
				oneoffSql.toString(), Lesson.class);
		Query repeatQuery = getEntityManager().createNativeQuery(
				repeatSql.toString(), Lesson.class);
		int counter = 1;
		for (Status sta : status) {
			oneoffQuery.setParameter(counter, sta.name());
			repeatQuery.setParameter(counter, sta.name());
			counter += 1;
		}

		oneoffQuery.setParameter(counter, user.getId())
				.setParameter(counter + 1, fromDate)
				.setParameter(counter + 2, toDate).getResultList();

		repeatQuery.setParameter(counter, user.getId())
				.setParameter(counter + 1, toDate)
				.setParameter(counter + 2, fromDate).getResultList();

		List<Lesson> oneoffLessons = oneoffQuery.getResultList();
		List<Lesson> repeatLessons = repeatQuery.getResultList();

		oneoffLessons.addAll(repeatLessons);
		return oneoffLessons;
	}

	@Override
	public List<Lesson> findRequested(Tutor tutor) {
		String sql = "select l from Lesson l " + "where l.tutor = :tutor "
				+ "and l.status = :stat order by l.requestDate";

		return getEntityManager().createQuery(sql, Lesson.class)
				.setParameter("tutor", tutor)
				.setParameter("stat", Status.REQUESTED).getResultList();
	}
}

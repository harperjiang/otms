package org.harper.otms.calendar.dao.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.harper.otms.auth.entity.User;
import org.harper.otms.calendar.dao.LessonDao;
import org.harper.otms.calendar.entity.Lesson;
import org.harper.otms.calendar.entity.Lesson.Status;
import org.harper.otms.calendar.service.util.DateUtil;
import org.harper.otms.common.dao.JpaDao;

public class JpaLessonDao extends JpaDao<Lesson> implements LessonDao {

	@SuppressWarnings("unchecked")
	protected List<Lesson> findOneoffLessonWithin(User user, Date fromDate,
			Date toDate, Status status) {
		StringBuilder sql = new StringBuilder("select l.* from lesson l")
				.append(" join calendar_entry ce")
				.append(" on ce.id = l.calendar").append(" where l.status = ?")
				.append(" and ce.calendar_type = 'ONEOFF' and");

		if ("tutor".equals(user.getType())) {
			sql.append(" l.tutor_id = ?");
		} else {
			sql.append(" l.client_id = ?");
		}
		sql.append(" and ce.oneoff_to_time >= ? and ce.oneoff_from_time <= ?");
		Query query = getEntityManager().createNativeQuery(sql.toString(),
				Lesson.class);
		query.setParameter(1, status.name());
		query.setParameter(2, user.getId());
		query.setParameter(3, fromDate);
		query.setParameter(4, toDate);

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	protected List<Lesson> findRepeatedLessonWithin(User user, Date fromDate,
			Date toDate, Status status) {
		StringBuilder sql = new StringBuilder("select l.* from lesson l")
				.append(" join calendar_entry ce")
				.append(" on ce.id = l.calendar").append(" where l.status = ?")
				.append(" and ce.calendar_type = 'REPEAT' and");

		if ("tutor".equals(user.getType())) {
			sql.append(" l.tutor_id = ?");
		} else {
			sql.append(" l.client_id = ?");
		}
		sql.append(" and ce.repeat_stop_date >= ? and ce.repeat_start_date <= ?");
		Query query = getEntityManager().createNativeQuery(sql.toString(),
				Lesson.class);
		query.setParameter(1, status.name());
		query.setParameter(2, user.getId());
		query.setParameter(3, DateUtil.truncate(fromDate));
		query.setParameter(4, DateUtil.dayend(toDate));

		return query.getResultList();
	}

	@Override
	public List<Lesson> findWithin(User user, Date fromDate, Date toDate,
			Status status) {
		if (fromDate.after(toDate)) {
			return new ArrayList<Lesson>();
		}
		List<Lesson> oneoffLessons = findOneoffLessonWithin(user, fromDate,
				toDate, status);
		List<Lesson> repeatLessons = findRepeatedLessonWithin(user, fromDate,
				toDate, status);

		oneoffLessons.addAll(repeatLessons);
		return oneoffLessons;
	}

	@Override
	public List<Lesson> findRequested(User user) {
		String sql = null;
		if ("tutor".equals(user.getType())) {
			sql = "select l from Lesson l " + "where l.tutor.id = :userid "
					+ "and l.status = :stat order by l.requestDate";
		} else {
			sql = "select l from Lesson l " + "where l.client.id = :userid "
					+ "and l.status = :stat order by l.requestDate";
		}
		return getEntityManager().createQuery(sql, Lesson.class)
				.setParameter("userid", user.getId())
				.setParameter("stat", Status.REQUESTED).getResultList();
	}

}

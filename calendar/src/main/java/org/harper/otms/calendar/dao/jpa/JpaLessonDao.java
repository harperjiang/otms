package org.harper.otms.calendar.dao.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.harper.otms.auth.entity.User;
import org.harper.otms.calendar.dao.LessonDao;
import org.harper.otms.calendar.entity.Client;
import org.harper.otms.calendar.entity.Lesson;
import org.harper.otms.calendar.entity.Lesson.Status;
import org.harper.otms.calendar.entity.Tutor;
import org.harper.otms.calendar.service.util.DateUtil;
import org.harper.otms.common.dao.JpaDao;

public class JpaLessonDao extends JpaDao<Lesson> implements LessonDao {

	@SuppressWarnings("unchecked")
	protected List<Lesson> findOneoffLessonWithin(User tutor, User client, Date fromDate, Date toDate, Status status) {
		StringBuilder sql = new StringBuilder("select l.* from lesson l").append(" join calendar_entry ce")
				.append(" on ce.id = l.calendar").append(" where l.status = ?")
				.append(" and ce.calendar_type = 'ONEOFF'");

		if (tutor != null) {
			sql.append(" and l.tutor_id = ?");
		}
		if (client != null) {
			sql.append(" and l.client_id = ?");
		}
		sql.append(" and ce.oneoff_to_time >= ? and ce.oneoff_from_time <= ? order by ce.oneoff_from_time");
		Query query = getEntityManager().createNativeQuery(sql.toString(), Lesson.class);
		int counter = 1;
		query.setParameter(counter++, status.name());
		if (tutor != null)
			query.setParameter(counter++, tutor.getId());
		if (client != null)
			query.setParameter(counter++, client.getId());

		query.setParameter(counter++, fromDate);
		query.setParameter(counter++, toDate);

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	protected List<Lesson> findRepeatedLessonWithin(User tutor, User client, Date fromDate, Date toDate,
			Status status) {
		StringBuilder sql = new StringBuilder("select l.* from lesson l").append(" join calendar_entry ce")
				.append(" on ce.id = l.calendar").append(" where l.status = ?")
				.append(" and ce.calendar_type = 'REPEAT'");

		if (tutor != null) {
			sql.append(" and l.tutor_id = ?");
		}
		if (client != null) {
			sql.append(" and l.client_id = ?");
		}
		sql.append(" and ce.repeat_stop_date >= ? and ce.repeat_start_date <= ?");
		Query query = getEntityManager().createNativeQuery(sql.toString(), Lesson.class);
		int counter = 1;
		query.setParameter(counter++, status.name());
		if (tutor != null)
			query.setParameter(counter++, tutor.getId());
		if (client != null)
			query.setParameter(counter++, client.getId());
		query.setParameter(counter++, DateUtil.truncate(fromDate));
		query.setParameter(counter++, DateUtil.dayend(toDate));

		return query.getResultList();
	}

	@Override
	public List<Lesson> findWithin(User user, Date fromDate, Date toDate, Status status) {
		if (fromDate.after(toDate)) {
			return new ArrayList<Lesson>();
		}
		User tutor = User.TYPE_TUTOR.equals(user.getType()) ? user : null;
		User client = User.TYPE_CLIENT.equals(user.getType()) ? user : null;
		List<Lesson> oneoffLessons = findOneoffLessonWithin(tutor, client, fromDate, toDate, status);
		List<Lesson> repeatLessons = findRepeatedLessonWithin(tutor, client, fromDate, toDate, status);

		oneoffLessons.addAll(repeatLessons);
		return oneoffLessons;
	}

	@Override
	public List<Lesson> findByStatus(User user, Status status) {
		String sql = null;
		if ("tutor".equals(user.getType())) {
			sql = "select l from Lesson l " + "where l.tutor.id = :userid "
					+ "and l.status = :stat order by l.requestDate";
		} else {
			sql = "select l from Lesson l " + "where l.client.id = :userid "
					+ "and l.status = :stat order by l.requestDate";
		}
		return getEntityManager().createQuery(sql, Lesson.class).setParameter("userid", user.getId())
				.setParameter("stat", status).getResultList();
	}

	@Override
	public List<Lesson> findWithin(Tutor tutor, Client client, Date fromDate, Date toDate, Status status) {
		if (fromDate.after(toDate)) {
			return new ArrayList<Lesson>();
		}
		List<Lesson> oneoffLessons = findOneoffLessonWithin(tutor.getUser(), client.getUser(), fromDate, toDate,
				status);
		List<Lesson> repeatLessons = findRepeatedLessonWithin(tutor.getUser(), client.getUser(), fromDate, toDate,
				status);

		oneoffLessons.addAll(repeatLessons);
		return oneoffLessons;
	}

}

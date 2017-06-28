package org.harper.otms.calendar.dao.jpa;

import java.util.List;

import javax.persistence.TypedQuery;

import org.harper.otms.calendar.dao.FeedbackDao;
import org.harper.otms.calendar.entity.lesson.LessonFeedback;
import org.harper.otms.common.dao.JpaDao;

public class JpaFeedbackDao extends JpaDao<LessonFeedback> implements FeedbackDao {

	@Override
	public LessonFeedback findByLessonItemId(int id) {
		String sql = "select f from LessonFeedback f where f.lessonItem.id = :lid";
		return getSingleResult(getEntityManager().createQuery(sql, LessonFeedback.class).setParameter("lid", id));
	}

	@Override
	public List<LessonFeedback> findByTutor(int tutorId, int limit) {
		String sql = "select f from LessonFeedback f where f.tutor.id = :tid order by f.createTime desc";
		TypedQuery<LessonFeedback> query = getEntityManager().createQuery(sql, LessonFeedback.class).setParameter("tid",
				tutorId);
		if (limit > 0) {
			query.setMaxResults(limit);
		}
		return query.getResultList();
	}

	@Override
	public List<LessonFeedback> findByClient(int clientId, int limit) {
		String sql = "select f from LessonFeedback f where f.client.id = :cid order by f.createTime desc";
		TypedQuery<LessonFeedback> query = getEntityManager().createQuery(sql, LessonFeedback.class).setParameter("cid",
				clientId);
		if (limit > 0) {
			query.setMaxResults(limit);
		}
		return query.getResultList();
	}

}

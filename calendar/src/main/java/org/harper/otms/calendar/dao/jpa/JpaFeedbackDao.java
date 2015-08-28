package org.harper.otms.calendar.dao.jpa;

import org.harper.otms.calendar.dao.FeedbackDao;
import org.harper.otms.calendar.entity.Feedback;
import org.harper.otms.common.dao.JpaDao;

public class JpaFeedbackDao extends JpaDao<Feedback> implements FeedbackDao {

	@Override
	public Feedback findByLessonItemId(int id) {
		String sql = "select f from Feedback f where f.lessonItem.id = :lid";
		return getSingleResult(getEntityManager().createQuery(sql,
				Feedback.class).setParameter("lid", id));
	}

}

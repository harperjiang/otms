package org.harper.otms.calendar.dao.jpa;

import org.harper.otms.calendar.dao.FeedbackDao;
import org.harper.otms.calendar.entity.LessonFeedback;
import org.harper.otms.common.dao.JpaDao;

public class JpaFeedbackDao extends JpaDao<LessonFeedback> implements FeedbackDao {

	@Override
	public LessonFeedback findByLessonItemId(int id) {
		String sql = "select f from Feedback f where f.lessonItem.id = :lid";
		return getSingleResult(getEntityManager().createQuery(sql,
				LessonFeedback.class).setParameter("lid", id));
	}

}

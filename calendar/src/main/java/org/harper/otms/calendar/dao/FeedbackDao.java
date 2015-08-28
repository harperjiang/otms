package org.harper.otms.calendar.dao;

import org.harper.otms.calendar.entity.Feedback;
import org.harper.otms.common.dao.Dao;

public interface FeedbackDao extends Dao<Feedback> {

	Feedback findByLessonItemId(int id);
}

package org.harper.otms.calendar.dao;

import org.harper.otms.calendar.entity.LessonFeedback;
import org.harper.otms.common.dao.Dao;

public interface FeedbackDao extends Dao<LessonFeedback> {

	LessonFeedback findByLessonItemId(int id);
}

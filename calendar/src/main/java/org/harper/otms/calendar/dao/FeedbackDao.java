package org.harper.otms.calendar.dao;

import java.util.List;

import org.harper.otms.calendar.entity.lesson.LessonFeedback;
import org.harper.otms.common.dao.Dao;

public interface FeedbackDao extends Dao<LessonFeedback> {

	LessonFeedback findByLessonItemId(int id);

	List<LessonFeedback> findByTutor(int tutorId, int limit);

	List<LessonFeedback> findByClient(int clientId, int limit);
}

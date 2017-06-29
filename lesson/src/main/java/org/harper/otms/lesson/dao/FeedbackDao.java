package org.harper.otms.lesson.dao;

import java.util.List;

import org.harper.otms.common.dao.Dao;
import org.harper.otms.lesson.entity.LessonFeedback;

public interface FeedbackDao extends Dao<LessonFeedback> {

	LessonFeedback findByLessonItemId(int id);

	List<LessonFeedback> findByTutor(int tutorId, int limit);

	List<LessonFeedback> findByClient(int clientId, int limit);
}

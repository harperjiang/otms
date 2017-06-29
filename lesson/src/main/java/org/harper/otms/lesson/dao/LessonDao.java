package org.harper.otms.lesson.dao;

import java.util.Date;
import java.util.List;

import org.harper.otms.auth.entity.User;
import org.harper.otms.common.dao.Dao;
import org.harper.otms.lesson.entity.Lesson;
import org.harper.otms.lesson.entity.Lesson.Status;
import org.harper.otms.profile.entity.Client;
import org.harper.otms.profile.entity.Tutor;

public interface LessonDao extends Dao<Lesson> {

	List<Lesson> findWithin(User user, Date fromDate, Date toDate, Status status);

	List<Lesson> findWithin(Tutor tutor, Client client, Date fromDate, Date toDate, Status status);

	List<Lesson> findByStatus(User user, Status status);

}

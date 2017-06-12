package org.harper.otms.calendar.dao;

import java.util.Date;
import java.util.List;

import org.harper.otms.auth.entity.User;
import org.harper.otms.calendar.entity.Client;
import org.harper.otms.calendar.entity.Lesson;
import org.harper.otms.calendar.entity.Lesson.Status;
import org.harper.otms.calendar.entity.Tutor;
import org.harper.otms.common.dao.Dao;

public interface LessonDao extends Dao<Lesson> {

	List<Lesson> findWithin(User user, Date fromDate, Date toDate, Status status);

	List<Lesson> findWithin(Tutor tutor, Client client, Date fromDate, Date toDate, Status status);

	List<Lesson> findByStatus(User user, Status status);

}

package org.harper.otms.calendar.dao;

import java.util.Date;
import java.util.List;

import org.harper.otms.auth.entity.User;
import org.harper.otms.calendar.entity.Lesson;
import org.harper.otms.calendar.entity.Snapshot;
import org.harper.otms.common.dao.Dao;

public interface SnapshotDao extends Dao<Snapshot> {

	List<Snapshot> findWithin(User user, Date fromDate, Date toDate);

	List<Lesson> findForComment(User user);
}

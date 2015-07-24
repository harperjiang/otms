package org.harper.otms.calendar.dao.jpa;

import java.util.Date;
import java.util.List;

import org.harper.otms.auth.entity.User;
import org.harper.otms.calendar.dao.SnapshotDao;
import org.harper.otms.calendar.entity.Lesson;
import org.harper.otms.calendar.entity.Snapshot;
import org.harper.otms.common.dao.JpaDao;

public class JpaSnapshotDao extends JpaDao<Snapshot> implements SnapshotDao {

	@Override
	@SuppressWarnings("unchecked")
	public List<Snapshot> findWithin(User part, Date fromDate, Date toDate) {
		String sql = "select * from snapshot s where"
				+ " s.to_time > ? and s.from_time < ? and"
				+ ("tutor".equals(part.getType()) ? " s.tutor_id = ?"
						: " s.client_id = ?");
		return (List<Snapshot>) getEntityManager()
				.createNativeQuery(sql, Snapshot.class)
				.setParameter(1, fromDate).setParameter(2, toDate)
				.setParameter(3, part.getId()).getResultList();
	}

	@Override
	public List<Lesson> findForComment(User user) {
		return null;
	}
}

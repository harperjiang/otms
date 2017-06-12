package org.harper.otms.calendar.dao.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;

import org.harper.otms.auth.entity.User;
import org.harper.otms.calendar.dao.LessonItemDao;
import org.harper.otms.calendar.entity.Client;
import org.harper.otms.calendar.entity.LessonItem;
import org.harper.otms.calendar.entity.LessonItem.Status;
import org.harper.otms.calendar.entity.Tutor;
import org.harper.otms.common.dao.JpaDao;
import org.harper.otms.common.dto.PagingDto;

public class JpaLessonItemDao extends JpaDao<LessonItem> implements LessonItemDao {

	@Override
	public List<LessonItem> findWithin(User user, Date fromDate, Date toDate, Status status, PagingDto paging) {
		if (fromDate != null && fromDate.after(toDate)) {
			return new ArrayList<LessonItem>();
		}

		StringBuilder sql = new StringBuilder().append("select li from LessonItem li")
				.append(" where li.status = :status").append(" and li.fromTime <= :toDate");
		if (fromDate != null) {
			sql.append(" and li.toTime >= :fromDate");
		}
		if (User.TYPE_CLIENT.equals(user.getType())) {
			sql.append(" and li.lesson.client.id =:id");
		} else if (User.TYPE_TUTOR.equals(user.getType())) {
			sql.append(" and li.lesson.tutor.id =:id");
		}

		TypedQuery<LessonItem> query = getEntityManager().createQuery(sql.toString(), LessonItem.class)
				.setParameter("status", status).setParameter("toDate", toDate);
		if (User.TYPE_CLIENT.equals(user.getType()) || User.TYPE_CLIENT.equals(user.getType()))
			query.setParameter("id", user.getId());
		if (fromDate != null)
			query.setParameter("fromDate", fromDate);
		if (paging == null) {
			return query.getResultList();
		} else {
			return pagingQuery(query, paging);
		}
	}

	@Override
	public List<LessonItem> findWithin(Tutor tutor, Client client, Date fromDate, Date toDate) {
		if (fromDate != null && fromDate.after(toDate)) {
			return new ArrayList<LessonItem>();
		}

		StringBuilder sql = new StringBuilder().append("select li from LessonItem li")
				.append(" where li.status = :status").append(" and li.fromTime <= :toDate")
				.append(" and li.toTime >= :fromDate").append(" and li.lesson.client.id =:client_id")
				.append(" and li.lesson.tutor.id =:tutor_id");

		TypedQuery<LessonItem> query = getEntityManager().createQuery(sql.toString(), LessonItem.class)
				.setParameter("toDate", toDate).setParameter("tutor_id", tutor.getId())
				.setParameter("client_id", client.getId()).setParameter("fromDate", fromDate);
		return query.getResultList();
	}
}

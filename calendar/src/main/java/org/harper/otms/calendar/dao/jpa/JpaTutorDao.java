package org.harper.otms.calendar.dao.jpa;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;

import org.harper.otms.calendar.dao.TutorDao;
import org.harper.otms.calendar.entity.profile.Tutor;
import org.harper.otms.common.dao.JpaDao;

public class JpaTutorDao extends JpaDao<Tutor> implements TutorDao {

	@Override
	public Tutor findByName(String tutorName) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Tutor> cq = cb.createQuery(Tutor.class);
		Predicate p = cb.equal(cq.from(Tutor.class).get("user").get("name"),
				tutorName);
		cq.where(p);
		try {
			return getEntityManager().createQuery(cq).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public List<Tutor> findPopular() {
		String sql = "select t from Tutor t order by t.popularLevel";
		return getEntityManager().createQuery(sql, Tutor.class)
				.setMaxResults(10).getResultList();
	}
}

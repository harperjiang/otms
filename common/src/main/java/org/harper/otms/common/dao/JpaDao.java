package org.harper.otms.common.dao;

import java.lang.reflect.ParameterizedType;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.eclipse.persistence.internal.jpa.QueryImpl;
import org.eclipse.persistence.internal.sessions.AbstractRecord;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.queries.DatabaseQuery;
import org.eclipse.persistence.queries.ScrollableCursor;
import org.eclipse.persistence.sessions.Session;
import org.harper.otms.common.dto.PagingDto;

public abstract class JpaDao<T extends Entity> implements Dao<T> {

	private EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Cursor<T> all() {
		Class<T> param = getParamClass();
		ScrollableCursor sc = (ScrollableCursor) getEntityManager()
				.createQuery(
						MessageFormat.format("select i from {0} i",
								param.getSimpleName()))
				.setHint("eclipselink.cursor.scrollable", true)
				.getSingleResult();
		return new DefaultCursor<T>(sc);
	}

	public List<T> allatonce() {
		Class<T> param = getParamClass();
		return getEntityManager().createQuery(
				MessageFormat.format("select a from {0} a",
						param.getSimpleName()), param).getResultList();
	}

	@SuppressWarnings("unchecked")
	protected Class<T> getParamClass() {
		return (Class<T>) (((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0]);
	}

	public T findById(int id) {
		String sql = MessageFormat.format(
				"select t from {0} t where t.id = :id", getParamClass()
						.getSimpleName());
		try {
			return getEntityManager().createQuery(sql, getParamClass())
					.setParameter("id", id).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public T save(T object) {
		if (0 == object.getId()) {
			// Pre-acquire object id if using table sequence
			getEntityManager().persist(object);
		} else {
			object = getEntityManager().merge(object);
		}

		return object;
	}

	@Override
	public void delete(T target) {
		if (target != null) {
			getEntityManager().remove(target);
		}
	}

	public static final class DefaultCursor<T> implements Cursor<T>,
			Iterator<T> {

		static final int BUFFER_SIZE = 1000;

		private ScrollableCursor sc;

		private List<T> buffer = null;

		private int bufferPointer;

		private int remain;

		private DefaultCursor(ScrollableCursor sc) {
			this.sc = sc;
			this.remain = sc.size();
		}

		@Override
		public boolean hasNext() {
			return remain > 0;
		}

		@Override
		@SuppressWarnings("unchecked")
		public T next() {
			if (remain <= 0) {
				throw new IllegalStateException("No more element");
			}
			if (buffer == null || bufferPointer == buffer.size()) {
				buffer = (List<T>) sc.next(Math.min(BUFFER_SIZE, remain));
				bufferPointer = 0;
			}
			T object = buffer.get((bufferPointer++));
			remain--;
			return object;
		}

		public Iterator<T> iterator() {
			return this;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Not supported");
		}
	}

	protected T getSingleResult(TypedQuery<T> query) {
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	protected List<T> pagingQuery(TypedQuery<T> query, PagingDto paging) {

		Session session = getEntityManager().unwrap(Session.class);
		// Extract SQL from query
		QueryImpl queryImpl = (QueryImpl) query;
		DatabaseQuery databaseQuery = queryImpl.getDatabaseQuery();

		AbstractRecord row = databaseQuery.rowFromArguments(
				paramValues(queryImpl), (AbstractSession) session);

		String sql = databaseQuery.getTranslatedSQLString(session, row);

		String countSql = "select count(1) from (" + sql + ") i0";
		Query countQuery = getEntityManager().createNativeQuery(countSql);

		Long count = (Long) countQuery.getSingleResult();
		Long totalPage = count / paging.getPageSize()
				+ (count % paging.getPageSize() > 0 ? 1 : 0);
		paging.setTotalPage(totalPage.intValue());

		query.setFirstResult(paging.getCurrentPage() * paging.getPageSize());
		query.setMaxResults(paging.getPageSize());

		return query.getResultList();
	}

	protected List<Object> paramValues(QueryImpl query) {
		DatabaseQuery dbQuery = query.getDatabaseQuery();
		List<Object> values = new ArrayList<Object>();
		for (String arg : dbQuery.getArguments()) {
			values.add(query.getParameterValue(arg));
		}
		return values;
	}

	protected String bindingExp(int length) {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		for (int i = 0; i < length; i++) {
			sb.append("?").append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");
		return sb.toString();
	}
}

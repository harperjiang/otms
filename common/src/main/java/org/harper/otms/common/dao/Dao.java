package org.harper.otms.common.dao;

import java.util.List;

public interface Dao<T extends Entity> {

	public T findById(int id);

	public void delete(T target);

	public T save(T obj);

	public Cursor<T> all();

	public List<T> allatonce();

	public static interface Cursor<T> extends Iterable<T> {

	}
}

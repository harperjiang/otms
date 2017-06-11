package org.harper.otms.calendar.dao;

import org.harper.otms.common.dao.Entity;

/**
 * 
 * @author harper
 *
 */
public interface SchedulerDao {

	public void setupScheduler(Entity entity);

	public void cancelScheduler(Entity entity);
}

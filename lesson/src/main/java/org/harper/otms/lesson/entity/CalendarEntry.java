package org.harper.otms.lesson.entity;

import java.util.Date;
import java.util.TimeZone;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.harper.otms.common.dao.Entity;

@javax.persistence.Entity
@Table(name = "calendar_entry")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "calendar_type")
public abstract class CalendarEntry extends Entity {

	/**
	 * This function treats all date/time as a time from Zone-from and convert
	 * it to Zone-to
	 * 
	 * @param from
	 * @param to
	 */
	public abstract void convert(TimeZone from, TimeZone to);
	
	public abstract Date firstTime();
	
	public abstract Date lastTime();
}

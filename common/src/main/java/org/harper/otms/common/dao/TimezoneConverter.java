package org.harper.otms.common.dao;

import java.util.TimeZone;

import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.sessions.Session;

public class TimezoneConverter implements Converter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4657353570439135776L;

	@Override
	public Object convertDataValueToObjectValue(Object arg0, Session arg1) {
		if (arg0 == null)
			return null;
		return TimeZone.getTimeZone(arg0.toString());
	}

	@Override
	public Object convertObjectValueToDataValue(Object arg0, Session arg1) {
		if (arg0 == null)
			return null;
		return ((TimeZone) arg0).getID();
	}

	@Override
	public void initialize(DatabaseMapping arg0, Session arg1) {
	}

	@Override
	public boolean isMutable() {
		return false;
	}

}

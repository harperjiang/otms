package org.harper.otms.common.dao;

import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.sessions.Session;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class JsonConverter implements Converter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5196069174367277334L;

	private Gson gson = new Gson();

	private JsonParser parser = new JsonParser();

	@Override
	public Object convertObjectValueToDataValue(Object objectValue,
			Session session) {
		return gson.toJson((JsonElement) objectValue);
	}

	@Override
	public Object convertDataValueToObjectValue(Object dataValue,
			Session session) {
		if (dataValue == null)
			return null;
		return parser.parse(String.valueOf(dataValue));
	}

	@Override
	public boolean isMutable() {
		return true;
	}

	@Override
	public void initialize(DatabaseMapping mapping, Session session) {
	}

}

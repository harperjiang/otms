package org.harper.otms.calendar.entity.setting;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

import org.harper.otms.common.dao.Entity;

@javax.persistence.Entity
@Table(name = "timesheet")
public class Timesheet extends Entity {

	public static String DEFAULT = "";

	static {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 48; j++) {
				builder.append("0");
			}
		}
		DEFAULT = builder.toString();
	}

	@Column(name = "def_value")
	private String defaultValue = DEFAULT;

	@ElementCollection
	@CollectionTable(name = "timesheet_item", joinColumns = @JoinColumn(name = "timesheet_id"))
	@MapKeyColumn(name = "ref_date")
	@Column(name = "value")
	private Map<Date, String> values = new HashMap<Date, String>();

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Map<Date, String> getValues() {
		return values;
	}

}

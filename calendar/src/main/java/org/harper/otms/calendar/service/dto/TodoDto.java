package org.harper.otms.calendar.service.dto;

import org.harper.otms.calendar.entity.Todo;

import com.google.gson.Gson;

public class TodoDto {

	private String type;

	private int refId;

	private String context;

	public TodoDto() {
		super();
	}

	public void from(Todo item) {
		this.setType(item.getType());
		// for (String dkey : DATE_KEYS) {
		// Date oldDate = new Date(item.getContext().get(dkey).getAsLong());
		// Date newDate = DateUtil.convert(oldDate, gmt, viewer.getTimezone());
		// item.getContext().
		// }
		this.setRefId(item.getRefId());
		this.setContext(new Gson().toJson(item.getContext()));
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public int getRefId() {
		return refId;
	}

	public void setRefId(int refId) {
		this.refId = refId;
	}

}

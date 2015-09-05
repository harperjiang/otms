package org.harper.otms.calendar.service.dto;

import org.harper.otms.common.dto.RequestDto;

public class ContactUsDto extends RequestDto {

	@Override
	public boolean needValidation() {
		return false;
	}

	private String from;

	private String email;

	private String content;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}

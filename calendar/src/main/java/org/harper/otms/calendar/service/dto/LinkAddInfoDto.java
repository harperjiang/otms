package org.harper.otms.calendar.service.dto;

import org.harper.otms.common.dto.RequestDto;

public class LinkAddInfoDto extends RequestDto {

	private String username;

	private String timezone;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

}

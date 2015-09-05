package org.harper.otms.auth.service.dto;

import org.harper.otms.common.dto.RequestDto;

public class CreateUserDto extends RequestDto {

	private String username;

	private String displayName;

	private String email;

	private String password;

	private String timezone;

	private String type;

	private boolean linkUser;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isLinkUser() {
		return linkUser;
	}

	public void setLinkUser(boolean linkUser) {
		this.linkUser = linkUser;
	}

}

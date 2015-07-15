package org.harper.otms.auth.entity;

import java.util.TimeZone;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.harper.otms.common.dao.Entity;

@MappedSuperclass
public class User extends Entity {

	@Column(name = "name")
	private String name;

	@Column(name = "display_name")
	private String displayName;

	@Column(name = "password")
	private String password;

	@Column(name = "email")
	private String email;

	@Column(name = "time_zone", columnDefinition = "VARCHAR(50)")
	private TimeZone timezone;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}

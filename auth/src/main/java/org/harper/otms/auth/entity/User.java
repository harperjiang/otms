package org.harper.otms.auth.entity;

import java.util.TimeZone;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Convert;
import org.harper.otms.auth.external.ExternalSystem;
import org.harper.otms.common.dao.Entity;

@javax.persistence.Entity
@Table(name = "user")
public class User extends Entity {

	@Column(name = "name")
	private String name;

	@Column(name = "display_name")
	private String displayName;

	@Column(name = "password")
	private String password;

	@Column(name = "email")
	private String email;

	@Column(name = "type")
	private String type;

	@Column(name = "time_zone", columnDefinition = "VARCHAR(50)")
	@Convert("timezoneConverter")
	private TimeZone timezone;

	@Column(name = "activated")
	private boolean activated;

	@Column(name = "activate_code")
	private String activationCode;

	/*
	 * Allow user to login with id from other system
	 */
	@Column(name = "source_system")
	@Enumerated(EnumType.STRING)
	private ExternalSystem sourceSystem;

	@Column(name = "source_id")
	private String sourceId;

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

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public TimeZone getTimezone() {
		return timezone;
	}

	public void setTimezone(TimeZone timezone) {
		this.timezone = timezone;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public String getActivationCode() {
		return activationCode;
	}

	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}

	public ExternalSystem getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(ExternalSystem sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public static final String TYPE_ADMIN = "admin";
	
	public static final String TYPE_CLIENT = "client";
	
	public static final String TYPE_TUTOR = "tutor";
}

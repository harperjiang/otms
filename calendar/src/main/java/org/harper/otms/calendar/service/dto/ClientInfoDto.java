package org.harper.otms.calendar.service.dto;

import java.util.TimeZone;

import org.harper.otms.calendar.entity.Client;

public class ClientInfoDto {

	private int clientId;

	private String displayName;

	private String username;

	private String timezone;

	private String email;

	private String pictureUrl;

	private String statement;

	private boolean emailClass;

	private boolean emailSchedule;

	private boolean emailBooking;

	public void from(Client client) {
		setClientId(client.getId());
		setStatement(client.getStatement());
		setUsername(client.getUser().getName());
		setDisplayName(client.getUser().getDisplayName());
		setTimezone(client.getUser().getTimezone().getID());
		setEmail(client.getUser().getEmail());
		setPictureUrl(client.getPictureUrl());

		boolean[] emailPref = client.getEmailSettings();

		setEmailClass(emailPref[0]);
		setEmailSchedule(emailPref[1]);
		setEmailBooking(emailPref[2]);
	}

	public void to(Client client) {
		client.getUser().setDisplayName(getDisplayName());
		client.getUser().setTimezone(TimeZone.getTimeZone(getTimezone()));
		client.getUser().setEmail(getEmail());

		client.setStatement(getStatement());
		client.setPictureUrl(getPictureUrl());

		client.setEmailSetting(new boolean[] { isEmailClass(), isEmailSchedule(), isEmailBooking() });
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

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

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public boolean isEmailClass() {
		return emailClass;
	}

	public void setEmailClass(boolean emailClass) {
		this.emailClass = emailClass;
	}

	public boolean isEmailSchedule() {
		return emailSchedule;
	}

	public void setEmailSchedule(boolean emailSchedule) {
		this.emailSchedule = emailSchedule;
	}

	public boolean isEmailBooking() {
		return emailBooking;
	}

	public void setEmailBooking(boolean emailBooking) {
		this.emailBooking = emailBooking;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}

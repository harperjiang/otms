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

	public void from(Client client) {
		setClientId(client.getId());
		setStatement(client.getStatement());
		setUsername(client.getUser().getName());
		setDisplayName(client.getUser().getDisplayName());
		setTimezone(client.getUser().getTimezone().getID());
		setEmail(client.getUser().getEmail());
		setPictureUrl(client.getPictureUrl());

	}

	public void to(Client client) {
		client.getUser().setDisplayName(getDisplayName());
		client.getUser().setTimezone(TimeZone.getTimeZone(getTimezone()));
		client.getUser().setEmail(getEmail());

		client.setStatement(getStatement());
		client.setPictureUrl(getPictureUrl());
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

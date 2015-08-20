package org.harper.otms.calendar.service.dto;

import java.util.TimeZone;

import org.harper.otms.calendar.entity.Client;

public class ClientInfoDto {

	private int clientId;

	private String displayName;

	private String username;

	private String timezone;

	private String description;

	private String email;

	private String pictureUrl;

	private String audioText;

	private String audioUrl;

	public void from(Client client) {
		setClientId(client.getId());
		setDescription(client.getDescription());
		setUsername(client.getUser().getName());
		setDisplayName(client.getUser().getDisplayName());
		setTimezone(client.getUser().getTimezone().getID());
		setEmail(client.getUser().getEmail());
		setPictureUrl(client.getPictureUrl());
		setAudioText(client.getAudioText());
		setAudioUrl(client.getAudioUrl());
	}

	public void to(Client client) {
		client.getUser().setDisplayName(getDisplayName());
		client.getUser().setTimezone(TimeZone.getTimeZone(getTimezone()));
		client.getUser().setEmail(getEmail());
		
		client.setDescription(getDescription());
		client.setAudioText(getAudioText());
		client.setAudioUrl(getAudioUrl());
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getAudioText() {
		return audioText;
	}

	public void setAudioText(String audioText) {
		this.audioText = audioText;
	}

	public String getAudioUrl() {
		return audioUrl;
	}

	public void setAudioUrl(String audioUrl) {
		this.audioUrl = audioUrl;
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

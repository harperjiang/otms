package org.harper.otms.calendar.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.harper.otms.auth.entity.User;

@Entity
@Table(name = "client")
public class Client extends org.harper.otms.common.dao.Entity {

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "description")
	private String description;

	@Column(name = "picture_url")
	private String pictureUrl;

	@Column(name = "statement")
	private String statement;

	@Column(name = "audio_url")
	private String audioUrl;

	@Column(name = "audio_text")
	private String audioText;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		this.setId(user.getId());
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

	public String getAudioUrl() {
		return audioUrl;
	}

	public void setAudioUrl(String audioUrl) {
		this.audioUrl = audioUrl;
	}

	public String getAudioText() {
		return audioText;
	}

	public void setAudioText(String audioText) {
		this.audioText = audioText;
	}

}

package org.harper.otms.calendar.entity;

import javax.persistence.Column;
import javax.persistence.Table;

import org.harper.otms.common.dao.Entity;

@javax.persistence.Entity
@Table(name = "contact_us")
public class ContactUs extends Entity {

	@Column(name = "from_name")
	private String from;

	@Column(name = "email")
	private String email;

	@Column(name = "user_id")
	private int userId;

	@Column(name = "content")
	private String content;

	@Column(name = "status")
	private int status;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}

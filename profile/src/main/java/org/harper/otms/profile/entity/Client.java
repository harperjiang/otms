package org.harper.otms.profile.entity;

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

	@Column(name = "picture_url")
	private String pictureUrl;

	@Column(name = "statement")
	private String statement;

	@Column(name = "email_setting")
	private int emailSetting;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		this.setId(user.getId());
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public int getEmailSetting() {
		return emailSetting;
	}

	public void setEmailSetting(int emailSetting) {
		this.emailSetting = emailSetting;
	}

	public void setEmailSetting(boolean[] emailSetting) {
		int sum = 0;
		for (int i = 0; i < emailSetting.length; i++) {
			sum |= (emailSetting[i] ? 1 : 0) << i;
		}
		this.emailSetting = sum;
	}

	protected static int CLIENT_EMAIL_SIZE = 3;

	public boolean[] getEmailSettings() {
		boolean[] result = new boolean[CLIENT_EMAIL_SIZE];
		for (int i = 0; i < result.length; i++) {
			result[i] = ((1 << i) | emailSetting) > 0;
		}
		return result;
	}

}

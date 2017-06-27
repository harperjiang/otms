package org.harper.otms.calendar.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.harper.otms.auth.entity.User;
import org.harper.otms.common.dao.Entity;

@javax.persistence.Entity
@Table(name = "tutor")
public class Tutor extends Entity {

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "picture_url")
	private String pictureUrl;

	@Column(name = "popular_level")
	private int popularLevel;

	@Column(name = "statement")
	private String statement;

	@Column(name = "description")
	private String description;

	@Column(name = "info_aboutme")
	private String aboutMe;

	@Column(name = "info_working")
	private String workingInfo;

	@Column(name = "info_edu")
	private String eduInfo;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "timesheet_id")
	private Timesheet timesheet = new Timesheet();

	@Transient
	private List<Date> holidays;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		this.setId(user.getId());
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timesheet getTimesheet() {
		return timesheet;
	}

	public void setTimesheet(Timesheet timesheet) {
		this.timesheet = timesheet;
	}

	public List<Date> getHolidays() {
		return holidays;
	}

	public void setHolidays(List<Date> holidays) {
		this.holidays = holidays;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public int getPopularLevel() {
		return popularLevel;
	}

	public void setPopularLevel(int popularLevel) {
		this.popularLevel = popularLevel;
	}

	public String getAboutMe() {
		return aboutMe;
	}

	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

	public String getWorkingInfo() {
		return workingInfo;
	}

	public void setWorkingInfo(String workingInfo) {
		this.workingInfo = workingInfo;
	}

	public String getEduInfo() {
		return eduInfo;
	}

	public void setEduInfo(String eduInfo) {
		this.eduInfo = eduInfo;
	}

}

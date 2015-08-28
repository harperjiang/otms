package org.harper.otms.calendar.service.dto;

import java.util.TimeZone;

import org.harper.otms.calendar.entity.Tutor;

public class TutorInfoDto {
	
	private int tutorId;

	private String displayName;

	private String username;

	private String timezone;

	private String email;

	private String pictureUrl;

	private String description;
	
	private String statement;
	
	private String aboutMeInfo;
	
	private String workingInfo;
	
	private String eduInfo;
	
	private String videoIntroUrl;

	public void from(Tutor tutor) {
		setTutorId(tutor.getId());
		setUsername(tutor.getUser().getName());

		setDescription(tutor.getDescription());
		setDisplayName(tutor.getUser().getDisplayName());
		setTimezone(tutor.getUser().getTimezone().getID());
		setEmail(tutor.getUser().getEmail());
		setPictureUrl(tutor.getPictureUrl());
		setStatement(tutor.getStatement());
		setAboutMeInfo(tutor.getAboutMe());
		setWorkingInfo(tutor.getWorkingInfo());
		setEduInfo(tutor.getEduInfo());
	}

	public void to(Tutor tutor) {
		tutor.setPictureUrl(getPictureUrl());
		tutor.setStatement(getStatement());
		tutor.setDescription(getDescription());
		tutor.setAboutMe(getAboutMeInfo());
		tutor.setWorkingInfo(getWorkingInfo());
		tutor.setEduInfo(getEduInfo());
		tutor.getUser().setDisplayName(getDisplayName());
		tutor.getUser().setEmail(getEmail());
		tutor.getUser().setTimezone(TimeZone.getTimeZone(getTimezone()));
	}

	public int getTutorId() {
		return tutorId;
	}

	public void setTutorId(int tutorId) {
		this.tutorId = tutorId;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public String getVideoIntroUrl() {
		return videoIntroUrl;
	}

	public void setVideoIntroUrl(String videoIntroUrl) {
		this.videoIntroUrl = videoIntroUrl;
	}

	public String getAboutMeInfo() {
		return aboutMeInfo;
	}

	public void setAboutMeInfo(String aboutMeInfo) {
		this.aboutMeInfo = aboutMeInfo;
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

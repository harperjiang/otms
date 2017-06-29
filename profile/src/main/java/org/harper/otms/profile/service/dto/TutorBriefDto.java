package org.harper.otms.profile.service.dto;

import org.harper.otms.profile.entity.Tutor;

public class TutorBriefDto {

	private int tutorId;

	private String pictureUrl;

	private String name;

	private String statement;
	
	private double rate;
	
	private String displayName;

	public void from(Tutor tutor) {
		setTutorId(tutor.getId());
		setPictureUrl(tutor.getPictureUrl());
		setName(tutor.getUser().getName());
		setStatement(tutor.getStatement());
		setRate(tutor.getRating());
		setDisplayName(tutor.getUser().getDisplayName());
	}

	public int getTutorId() {
		return tutorId;
	}

	public void setTutorId(int tutorId) {
		this.tutorId = tutorId;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}

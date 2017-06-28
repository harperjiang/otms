package org.harper.otms.calendar.service.dto;

import org.harper.otms.calendar.entity.profile.Tutor;

public class TutorBriefDto {

	private int tutorId;

	private String pictureUrl;

	private String name;

	private String description;

	public void from(Tutor tutor) {
		setTutorId(tutor.getId());
		setPictureUrl(tutor.getPictureUrl());
		setName(tutor.getUser().getName());
		setDescription(tutor.getDescription());
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}

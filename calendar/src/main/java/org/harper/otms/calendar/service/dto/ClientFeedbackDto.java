package org.harper.otms.calendar.service.dto;

import org.harper.otms.common.dto.RequestDto;

public class ClientFeedbackDto extends RequestDto {

	private int lessonItemId;

	private boolean lessonSuccess;

	private int lessonRate;

	private int tutorRate;

	private int tutorNoattendRate;

	private int noAttendReason;

	private String comment;

	public int getLessonItemId() {
		return lessonItemId;
	}

	public void setLessonItemId(int lessonItemId) {
		this.lessonItemId = lessonItemId;
	}

	public boolean isLessonSuccess() {
		return lessonSuccess;
	}

	public void setLessonSuccess(boolean lessonSuccess) {
		this.lessonSuccess = lessonSuccess;
	}

	public int getLessonRate() {
		return lessonRate;
	}

	public void setLessonRate(int lessonRate) {
		this.lessonRate = lessonRate;
	}

	public int getTutorRate() {
		return tutorRate;
	}

	public void setTutorRate(int tutorRate) {
		this.tutorRate = tutorRate;
	}

	public int getTutorNoattendRate() {
		return tutorNoattendRate;
	}

	public void setTutorNoattendRate(int tutorNoattendRate) {
		this.tutorNoattendRate = tutorNoattendRate;
	}

	public int getNoAttendReason() {
		return noAttendReason;
	}

	public void setNoAttendReason(int noAttendReason) {
		this.noAttendReason = noAttendReason;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}

package org.harper.otms.calendar.service.dto;

import org.harper.otms.calendar.entity.Feedback;

public class FeedbackDto {

	private int id;

	private boolean success;

	private int failReason;

	private int tutorRate;

	private int lessonRate;

	private String comment;

	public void from(Feedback fb) {
		setId(fb.getId());
		setSuccess(fb.isSuccess());
		setFailReason(fb.getFailReason());
		setTutorRate(fb.getTutorRate());
		setLessonRate(fb.getLessonRate());
		setComment(fb.getComment());
	}

	public void to(Feedback fb) {
		throw new RuntimeException("not implemented");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getTutorRate() {
		return tutorRate;
	}

	public void setTutorRate(int tutorRate) {
		this.tutorRate = tutorRate;
	}

	public int getLessonRate() {
		return lessonRate;
	}

	public void setLessonRate(int lessonRate) {
		this.lessonRate = lessonRate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getFailReason() {
		return failReason;
	}

	public void setFailReason(int failReason) {
		this.failReason = failReason;
	}

}

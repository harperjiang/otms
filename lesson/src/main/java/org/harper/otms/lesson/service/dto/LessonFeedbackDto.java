package org.harper.otms.lesson.service.dto;

import java.util.Date;
import java.util.TimeZone;

import org.harper.otms.auth.entity.User;
import org.harper.otms.common.util.DateUtil;
import org.harper.otms.lesson.entity.LessonFeedback;

public class LessonFeedbackDto {

	private int id;

	private boolean success;

	private int failReason;

	private int tutorRate;

	private int lessonRate;

	private String comment;

	private Date createTime;

	private String tutorName;
	
	private String clientName;
	
	public void from(LessonFeedback fb, User viewer) {
		setId(fb.getId());
		setTutorName(fb.getTutor().getUser().getName());
		setClientName(fb.getClient().getUser().getName());
		setSuccess(fb.isSuccess());
		setFailReason(fb.getFailReason());
		setTutorRate(fb.getTutorRate());
		setLessonRate(fb.getLessonRate());
		setComment(fb.getComment());
		setCreateTime(DateUtil.convert(fb.getCreateTime(), TimeZone.getTimeZone("UTC"), viewer.getTimezone()));
	}

	public void to(LessonFeedback fb) {
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getTutorName() {
		return tutorName;
	}

	public void setTutorName(String tutorName) {
		this.tutorName = tutorName;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

}

package org.harper.otms.lesson.service.dto;

import org.harper.otms.common.dto.RequestDto;

public class GetTutorFeedbacksDto extends RequestDto {

	private int tutorId;

	private int limit;

	public int getTutorId() {
		return tutorId;
	}

	public void setTutorId(int tutorId) {
		this.tutorId = tutorId;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	@Override
	public boolean needValidation() {
		return false;
	}
}

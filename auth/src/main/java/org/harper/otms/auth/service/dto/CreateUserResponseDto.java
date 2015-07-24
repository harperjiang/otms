package org.harper.otms.auth.service.dto;

import org.harper.otms.common.dto.ResponseDto;

public class CreateUserResponseDto extends ResponseDto {

	private int userId;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	
}

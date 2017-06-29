package org.harper.otms.profile.service.dto;

import org.harper.otms.common.dto.ResponseDto;

public class LinkAddInfoResponseDto extends ResponseDto {

	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public LinkAddInfoResponseDto() {
		super();
	}

	public LinkAddInfoResponseDto(int errCode) {
		super(errCode);
	}

}

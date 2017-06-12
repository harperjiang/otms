package org.harper.otms.calendar.service.dto;

import org.harper.otms.common.dto.ResponseDto;

public class UploadProfileImageResponseDto extends ResponseDto {

	private String imageUrl;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

}

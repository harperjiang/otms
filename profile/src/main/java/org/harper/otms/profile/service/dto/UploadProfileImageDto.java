package org.harper.otms.profile.service.dto;

import java.io.FileInputStream;

import org.harper.otms.common.dto.RequestDto;

public class UploadProfileImageDto extends RequestDto {

	private FileInputStream image;

	public FileInputStream getImage() {
		return image;
	}

	public void setImage(FileInputStream image) {
		this.image = image;
	}

}

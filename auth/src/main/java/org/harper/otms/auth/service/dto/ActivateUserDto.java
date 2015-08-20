package org.harper.otms.auth.service.dto;

import org.harper.otms.common.dto.RequestDto;

public class ActivateUserDto extends RequestDto {

	@Override
	public boolean needValidation() {
		return false;
	}

	private String uuid;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}

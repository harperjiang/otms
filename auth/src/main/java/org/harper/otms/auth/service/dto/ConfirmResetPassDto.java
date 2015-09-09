package org.harper.otms.auth.service.dto;

import org.harper.otms.common.dto.RequestDto;

public class ConfirmResetPassDto extends RequestDto {

	@Override
	public boolean needValidation() {
		return false;
	}

	private String uuid;

	private String newpass;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getNewpass() {
		return newpass;
	}

	public void setNewpass(String newpass) {
		this.newpass = newpass;
	}

}

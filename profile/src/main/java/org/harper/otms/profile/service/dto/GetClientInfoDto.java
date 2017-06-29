package org.harper.otms.profile.service.dto;

import org.harper.otms.common.dto.RequestDto;

public class GetClientInfoDto extends RequestDto {

	private int clientId;

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

}

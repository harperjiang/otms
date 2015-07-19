package org.harper.otms.calendar.service.dto;

import org.harper.otms.common.dto.RequestDto;

public class SetupClientDto extends RequestDto {

	private ClientInfoDto clientInfo;

	public ClientInfoDto getClientInfo() {
		return clientInfo;
	}

	public void setClientInfo(ClientInfoDto clientInfo) {
		this.clientInfo = clientInfo;
	}

}

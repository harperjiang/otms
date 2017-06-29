package org.harper.otms.profile.service.dto;

import org.harper.otms.common.dto.ResponseDto;

public class GetClientInfoResponseDto extends ResponseDto {

	private ClientInfoDto clientInfo;

	public ClientInfoDto getClientInfo() {
		return clientInfo;
	}

	public void setClientInfo(ClientInfoDto clientInfo) {
		this.clientInfo = clientInfo;
	}

}

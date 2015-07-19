package org.harper.otms.calendar.service;

import org.harper.otms.calendar.service.dto.GetClientInfoDto;
import org.harper.otms.calendar.service.dto.GetClientInfoResponseDto;
import org.harper.otms.calendar.service.dto.SetupClientDto;
import org.harper.otms.calendar.service.dto.SetupClientResponseDto;

public interface ClientService {

	/**
	 * 
	 * @param request
	 * @return
	 */
	public GetClientInfoResponseDto getClientInfo(GetClientInfoDto request);

	/**
	 * 
	 * @param request
	 * @return
	 */
	public SetupClientResponseDto setupClient(SetupClientDto request);
}

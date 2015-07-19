package org.harper.otms.calendar.service.impl;

import org.harper.otms.calendar.dao.ClientDao;
import org.harper.otms.calendar.entity.Client;
import org.harper.otms.calendar.service.ClientService;
import org.harper.otms.calendar.service.ErrorCode;
import org.harper.otms.calendar.service.dto.ClientInfoDto;
import org.harper.otms.calendar.service.dto.GetClientInfoDto;
import org.harper.otms.calendar.service.dto.GetClientInfoResponseDto;
import org.harper.otms.calendar.service.dto.SetupClientDto;
import org.harper.otms.calendar.service.dto.SetupClientResponseDto;

public class DefaultClientService implements ClientService {

	@Override
	public GetClientInfoResponseDto getClientInfo(GetClientInfoDto request) {
		Client client = getClientDao().findById(request.getClientId());

		ClientInfoDto infoDto = new ClientInfoDto();
		infoDto.from(client);

		GetClientInfoResponseDto result = new GetClientInfoResponseDto();
		result.setClientInfo(infoDto);

		return result;
	}

	@Override
	public SetupClientResponseDto setupClient(SetupClientDto request) {
		if (request.getCurrentUser() != request.getClientInfo().getClientId()) {
			return new SetupClientResponseDto(ErrorCode.SYS_NO_AUTH);
		}
		Client client = getClientDao().findById(request.getCurrentUser());
		request.getClientInfo().to(client);
		return new SetupClientResponseDto();
	}

	private ClientDao clientDao;

	public ClientDao getClientDao() {
		return clientDao;
	}

	public void setClientDao(ClientDao clientDao) {
		this.clientDao = clientDao;
	}

}

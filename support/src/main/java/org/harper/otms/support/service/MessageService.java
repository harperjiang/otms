package org.harper.otms.support.service;

import org.harper.otms.support.service.dto.ContactUsDto;
import org.harper.otms.support.service.dto.ContactUsResponseDto;

public interface MessageService {

	public ContactUsResponseDto contactUs(ContactUsDto request);
}

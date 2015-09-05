package org.harper.otms.calendar.service;

import org.harper.otms.calendar.service.dto.ContactUsDto;
import org.harper.otms.calendar.service.dto.ContactUsResponseDto;

public interface MessageService {

	public ContactUsResponseDto contactUs(ContactUsDto request);
}

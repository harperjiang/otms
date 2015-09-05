package org.harper.otms.calendar.service.impl;

import org.harper.otms.calendar.dao.ContactUsDao;
import org.harper.otms.calendar.entity.ContactUs;
import org.harper.otms.calendar.service.MessageService;
import org.harper.otms.calendar.service.dto.ContactUsDto;
import org.harper.otms.calendar.service.dto.ContactUsResponseDto;

public class DefaultMessageService implements MessageService {

	@Override
	public ContactUsResponseDto contactUs(ContactUsDto request) {
		ContactUs cu = new ContactUs();
		cu.setFrom(request.getFrom());
		cu.setContent(request.getContent());
		cu.setEmail(request.getEmail());
		cu.setUserId(request.getCurrentUser());
		getContactUsDao().save(cu);
		return new ContactUsResponseDto();
	}

	private ContactUsDao contactUsDao;

	public ContactUsDao getContactUsDao() {
		return contactUsDao;
	}

	public void setContactUsDao(ContactUsDao contactUsDao) {
		this.contactUsDao = contactUsDao;
	}

}

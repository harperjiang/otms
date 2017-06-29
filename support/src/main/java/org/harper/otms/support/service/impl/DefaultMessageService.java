package org.harper.otms.support.service.impl;

import org.harper.otms.support.dao.ContactUsDao;
import org.harper.otms.support.entity.ContactUs;
import org.harper.otms.support.service.MessageService;
import org.harper.otms.support.service.dto.ContactUsDto;
import org.harper.otms.support.service.dto.ContactUsResponseDto;

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

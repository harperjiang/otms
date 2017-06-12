package org.harper.otms.common.mail;

import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;

public class VelocityHtmlMessagePreparator implements MimeMessagePreparator {

	private String sender = "TutorCan<webmaster@tutorcan.com>";

	private String[] receiver;

	private String title;

	private String template;

	private Map<String, Object> params;

	public VelocityHtmlMessagePreparator(String[] receiver, String title,
			String template, Map<String, Object> params) {
		super();
		this.receiver = receiver;
		this.title = title;
		this.template = template;
		this.params = params;
	}

	@Override
	public void prepare(MimeMessage msg) throws Exception {
		MimeMessageHelper message = new MimeMessageHelper(msg);
		message.setFrom(this.sender);
		message.setTo(this.receiver);
		message.setSubject(this.title);
		message.setText(VelocityEngineUtils.mergeTemplateIntoString(
				SharedVelocityEngine.get(), template, "utf8", params), true);
	}
}

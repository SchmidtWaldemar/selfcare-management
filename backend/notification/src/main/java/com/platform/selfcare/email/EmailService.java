package com.platform.selfcare.email;

import java.nio.charset.StandardCharsets;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

	private final JavaMailSender javaMailSender;
	
	private final SpringTemplateEngine templateEngine;
	
	@Async
	public void sendRegistrationMail(String mailTo) throws MessagingException {
		
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
		messageHelper.setFrom("test@test.de");
		
		final String templateName = "register-successful.html";
		messageHelper.setSubject("Herzlich Willkommen beim Selbsthilfebereich");
		
		try {
			String htmlTempsate = templateEngine.process(templateName, new Context());
			messageHelper.setTo(mailTo);
			messageHelper.setText(htmlTempsate, true);
			javaMailSender.send(mimeMessage);
		} catch (MessagingException e) {
			log.error("Mail Versand bei Registrierung fehlgeschlagen", e);
		}
	}
	
	@Async
	public void sendNewGroupMemberMailMessage(String mailTo) throws MessagingException {
		
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
		messageHelper.setFrom("test@test.de");
		
		final String templateName = "membership-successful.html";
		messageHelper.setSubject("Weiteres Mitglied in der Gruppe");
		
		try {
			String htmlTempsate = templateEngine.process(templateName, new Context());
			messageHelper.setTo(mailTo);
			messageHelper.setText(htmlTempsate, true);
			javaMailSender.send(mimeMessage);
		} catch (MessagingException e) {
			log.error("Mail Versand bei Mitglieder Zuwachs E-Mail fehlgeschlagen", e);
		}
	}
}

package com.bankapp.notification_service.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.bankapp.notification_service.config.ApplicationProperties;
import com.bankapp.notification_service.dto.NotificationRequest;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

import java.util.HashMap;
import java.util.Map;


@Service
public class NotificationService {
	
	private static final String NOTIFICATION_TEMPLATE = "notification.html";
	private final JavaMailSender mailSender;
	private final SpringTemplateEngine templateEngine;
	private final ApplicationProperties applicationProperties;
	
	public NotificationService(JavaMailSender mailSender, SpringTemplateEngine templateEngine, ApplicationProperties applicationProperties) {
		this.mailSender = mailSender;
		this.templateEngine = templateEngine;
		this.applicationProperties = applicationProperties;
	}

	@Async
	public void sendNotification(NotificationRequest notification) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MULTIPART_MODE_MIXED, UTF_8.name());
			Map<String, Object> properties = new HashMap<>();
			properties.put("data", notification.getBody());
			send(notification.getTo(), notification.getSubject(), mimeMessage, helper, properties);
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void send(String to, String subject, MimeMessage mimeMessage, @NotNull MimeMessageHelper helper,
			Map<String, Object> properties) throws MessagingException {
		Context context = new Context();
		context.setVariables(properties);
		
		helper.setFrom(applicationProperties.getSystemEmail());
		helper.setTo(to);
		helper.setSubject(subject);
		
		String template = templateEngine.process(NOTIFICATION_TEMPLATE, context);
		helper.setText(template, true);
		mailSender.send(mimeMessage);
	}
	
	

}

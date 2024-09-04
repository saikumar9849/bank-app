package com.bankapp.notification_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationProperties {
	
	@Value("${application.mail.email-system}")
	private String systemEmail;

	public String getSystemEmail() {
		return systemEmail;
	}

}

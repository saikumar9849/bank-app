package com.bankapp.notification_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class NotificationRequest {
	
	@Email(message="e-mail is not well formated")
	@NotBlank(message = "e-mail is mandatory: it can not be blank")
	private String to;
	
	@NotBlank(message = "subject is mandatory: it can not be blank")
	private String subject;
	
	@NotBlank(message = "body is mandatory: it can not be blank")
	private String  body;

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}

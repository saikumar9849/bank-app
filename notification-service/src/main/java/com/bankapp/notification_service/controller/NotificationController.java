package com.bankapp.notification_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankapp.notification_service.dto.NotificationRequest;
import com.bankapp.notification_service.service.NotificationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/mailing")
public class NotificationController {
	
	@Autowired
	NotificationService notificationService;
	
	@PostMapping("/send")
	public void send(@RequestBody @Valid NotificationRequest notification) {
		notificationService.sendNotification(notification);
	}

}

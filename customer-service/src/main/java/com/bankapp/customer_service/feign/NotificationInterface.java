package com.bankapp.customer_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.bankapp.customer_service.dto.NotificationRequest;

import jakarta.validation.Valid;

@FeignClient("NOTIFICATION-SERVICE")
public interface NotificationInterface {
	
	@PostMapping("/mailing/send")
	public void send(@RequestBody @Valid NotificationRequest notification);

}

package com.bankapp.customer_service.exception;

public class CustomerNotFound extends RuntimeException {
	public CustomerNotFound(String message) {
		super(message);
	}
}

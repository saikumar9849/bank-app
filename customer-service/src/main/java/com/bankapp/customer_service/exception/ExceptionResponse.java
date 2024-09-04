package com.bankapp.customer_service.exception;

public class ExceptionResponse extends RuntimeException {
	public ExceptionResponse(String message) {
		super(message);
	}
}

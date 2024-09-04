package com.bankapp.customer_service.exception;

public class LowBalance extends RuntimeException {
	public LowBalance(String message) {
		super(message);
	}

}

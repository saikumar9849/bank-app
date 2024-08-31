package com.bankapp.customer_service.dto;

import java.time.LocalDateTime;

import com.bankapp.customer_service.enumes.TransactionType;

public class TransactionResponse {
	
	private String id;
	private Integer fromAccount;
	private Integer toAccount;
	private TransactionType type;
	private LocalDateTime dateTime;
	private Integer amount;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getFromAccount() {
		return fromAccount;
	}
	public void setFromAccount(Integer fromAccount) {
		this.fromAccount = fromAccount;
	}
	public Integer getToAccount() {
		return toAccount;
	}
	public void setToAccount(Integer toAccount) {
		this.toAccount = toAccount;
	}
	public TransactionType getType() {
		return type;
	}
	public void setType(TransactionType type) {
		this.type = type;
	}
	public LocalDateTime getDateTime() {
		return dateTime;
	}
	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}

}

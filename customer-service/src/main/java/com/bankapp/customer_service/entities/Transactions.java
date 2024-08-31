package com.bankapp.customer_service.entities;

import java.time.LocalDateTime;

import com.bankapp.customer_service.enumes.TransactionType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Transactions {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	
	private LocalDateTime dateTime;
	private Integer amount;
	@Enumerated(EnumType.STRING)
	private TransactionType type;
	@ManyToOne
	private Account from;
	@ManyToOne
	private Account to;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public TransactionType getType() {
		return type;
	}
	public void setType(TransactionType type) {
		this.type = type;
	}
	public Account getFrom() {
		return from;
	}
	public void setFrom(Account from) {
		this.from = from;
	}
	public Account getTo() {
		return to;
	}
	public void setTo(Account to) {
		this.to = to;
	}

}

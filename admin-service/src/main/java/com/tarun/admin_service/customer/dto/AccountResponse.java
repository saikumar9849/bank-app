package com.tarun.admin_service.customer.dto;

import java.time.LocalDate;
import com.tarun.admin_service.enumes.AccountStatus;


public class AccountResponse {
	
	private Integer id;
	private  AccountStatus accountStatus;
	private Integer balance;
	private LocalDate createdDate;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public AccountStatus getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(AccountStatus accountStatus) {
		this.accountStatus = accountStatus;
	}
	public Integer getBalance() {
		return balance;
	}
	public void setBalance(Integer balance) {
		this.balance = balance;
	}
	public LocalDate getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}

}

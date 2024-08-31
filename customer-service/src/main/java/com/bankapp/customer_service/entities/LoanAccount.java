package com.bankapp.customer_service.entities;

import com.bankapp.customer_service.enumes.LoanStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class LoanAccount {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Enumerated(EnumType.STRING)
	private  LoanStatus loanStatus;
	
	private Integer amount;
	private Integer emi;
	private Integer tenureInMonths;
	private Integer rateOfInterest;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public LoanStatus getLoanStatus() {
		return loanStatus;
	}
	public void setLoanStatus(LoanStatus loanStatus) {
		this.loanStatus = loanStatus;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Integer getEmi() {
		return emi;
	}
	public void setEmi(Integer emi) {
		this.emi = emi;
	}
	public Integer getTenureInMonths() {
		return tenureInMonths;
	}
	public void setTenureInMonths(Integer tenureInMonths) {
		this.tenureInMonths = tenureInMonths;
	}
	public Integer getRateOfInterest() {
		return rateOfInterest;
	}
	public void setRateOfInterest(Integer rateOfInterest) {
		this.rateOfInterest = rateOfInterest;
	}
}

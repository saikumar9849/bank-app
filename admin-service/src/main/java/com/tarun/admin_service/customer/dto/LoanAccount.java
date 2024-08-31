package com.tarun.admin_service.customer.dto;

import com.tarun.admin_service.enumes.LoanStatus;

public class LoanAccount {
	private Integer id;
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

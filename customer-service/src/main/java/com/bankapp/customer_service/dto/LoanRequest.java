package com.bankapp.customer_service.dto;

public class LoanRequest {
	
	private Integer amount;
	private Integer tenureInMonths;
	private Integer rateOfInterest;
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
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

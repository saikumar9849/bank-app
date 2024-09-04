package com.bankapp.customer_service.dto;

import com.bankapp.customer_service.enumes.InvestmentType;

public class InvestmentRequest {
	
	private Integer amount;
	
	private InvestmentType investmentType;

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public InvestmentType getInvestmentType() {
		return investmentType;
	}

	public void setInvestmentType(InvestmentType investmentType) {
		this.investmentType = investmentType;
	}

}

package com.bankapp.customer_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bankapp.customer_service.dto.LoanCalculatorResponse;
import com.bankapp.customer_service.dto.LoanRequest;
import com.bankapp.customer_service.entities.LoanAccount;
import com.bankapp.customer_service.service.LoanService;

@RestController
@RequestMapping("/loans")
public class LoanController {
	
	@Autowired
	LoanService loanService;
	
	@PostMapping("/calculate-emi")
	public LoanCalculatorResponse emiCalculator(@RequestBody LoanRequest loanRequest) {
		return loanService.emiCalculator(loanRequest);
	}
	
	@PostMapping("/apply-loan/{customerId}")
	public LoanAccount applyLoan(@PathVariable Integer customerId,@RequestBody LoanRequest loanRequest) {
		return loanService.applyLoan(customerId,loanRequest);
	}
	
	@PutMapping("/loanapproval")
	public LoanAccount loanApproval(@RequestParam Integer customerId,@RequestParam String status) {
		return loanService.loanApproval(customerId, status);
	}
	
	@GetMapping("/loanDeatils/{customerId}")
	public LoanAccount loanDeatils(@PathVariable Integer customerId) {
		return loanService.getLoanDetails(customerId);
	}

}

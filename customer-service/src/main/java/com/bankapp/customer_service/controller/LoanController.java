package com.bankapp.customer_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.bankapp.customer_service.security.jwt.JwtUtils;
import com.bankapp.customer_service.service.LoanService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/loans")
public class LoanController {
	
	@Autowired
	LoanService loanService;
	
	@Autowired
    JwtUtils jwtUtils;
	
	@PostMapping("/calculate-emi")
	@SecurityRequirement(name = "Bearer Authentication")
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public LoanCalculatorResponse emiCalculator(@RequestBody LoanRequest loanRequest) {
		return loanService.emiCalculator(loanRequest);
	}
	
	@PostMapping("/apply-loan/{customerId}")
	@SecurityRequirement(name = "Bearer Authentication")
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public LoanAccount applyLoan(@PathVariable Integer customerId,@RequestBody LoanRequest loanRequest) {
		return loanService.applyLoan(customerId,loanRequest);
	}
	
	@GetMapping("/loanDeatils/{customerId}")
	@SecurityRequirement(name = "Bearer Authentication")
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public LoanAccount getLoanDeatils(@PathVariable Integer customerId) {
		return loanService.getLoanDetails(customerId);
	}
	
	@PutMapping("/admin/loanapproval")
	public ResponseEntity<LoanAccount> loanApproval(@RequestParam String token, @RequestParam Integer customerId,@RequestParam String status) {
		if(jwtUtils.validateJwtToken(token)) {
			return new ResponseEntity<>(loanService.loanApproval(customerId, status),HttpStatus.OK);
		}
		return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);
	}
	
	@GetMapping("/admin/loanDeatils/{customerId}")
	public ResponseEntity<LoanAccount> getLoanDeatils(@RequestParam String token, @PathVariable Integer customerId) {
		if(jwtUtils.validateJwtToken(token)) {
			return new ResponseEntity<>(loanService.getLoanDetails(customerId),HttpStatus.OK);
		}
		return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);
	}

}

package com.bankapp.customer_service.controller;

import java.util.List;

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

import com.bankapp.customer_service.dto.AccountResponse;
import com.bankapp.customer_service.dto.TransactionRequest;
import com.bankapp.customer_service.dto.TransactionResponse;
import com.bankapp.customer_service.security.jwt.JwtUtils;
import com.bankapp.customer_service.service.AccountService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/accounts")
public class AccountController {
	
	@Autowired
	AccountService accountService;
	
	@Autowired
    JwtUtils jwtUtils;
	
	@PostMapping("/deposit")
	@SecurityRequirement(name = "Bearer Authentication")
	@PreAuthorize("hasRole('ROLE_USER')")
	public TransactionResponse depositAmount(@RequestBody TransactionRequest transactionReq) {
		return accountService.depositAmount(transactionReq);
	}
	
	@PostMapping("/withdraw")
	@SecurityRequirement(name = "Bearer Authentication")
	@PreAuthorize("hasRole('ROLE_USER')")
	public TransactionResponse withdrawAmount(@RequestBody TransactionRequest transactionReq) {
		return accountService.withdrawAmount(transactionReq);
	}
	
	@PostMapping("/transfer")
	@SecurityRequirement(name = "Bearer Authentication")
	@PreAuthorize("hasRole('ROLE_USER')")
	public TransactionResponse transferAmount(@RequestBody TransactionRequest transactionReq) {
		return accountService.transferAmount(transactionReq);
	}
	
	@GetMapping("/transactions/{accountId}")
	@SecurityRequirement(name = "Bearer Authentication")
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public List<TransactionResponse> getTransactionsList(@PathVariable Integer accountId){
		return accountService.getTransactionsList(accountId);
	}
	
	@GetMapping("/getaccount/{customerId}")
	@SecurityRequirement(name = "Bearer Authentication")
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public AccountResponse getAccount(@PathVariable Integer customerId) {
		return accountService.getAccount(customerId);
	}
	
	@PutMapping("/admin/update-accountstatus")
	public ResponseEntity<AccountResponse> updateAccountStatus(@RequestParam String token, @RequestParam Integer customerId,@RequestParam String status) {
		if(jwtUtils.validateJwtToken(token)) {
			return new ResponseEntity<>(accountService.getAccount(customerId),HttpStatus.OK);
		}
		return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);
	}
	
	@GetMapping("/admin/getaccount/{customerId}")
	public ResponseEntity<AccountResponse> getAccount(@RequestParam String token, @PathVariable Integer customerId) {
		if(jwtUtils.validateJwtToken(token)) {
			return new ResponseEntity<>(accountService.getAccount(customerId),HttpStatus.OK);
		}
		return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);
	}

}

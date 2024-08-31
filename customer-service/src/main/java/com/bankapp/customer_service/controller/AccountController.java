package com.bankapp.customer_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.bankapp.customer_service.service.AccountService;

@RestController
@RequestMapping("/accounts")
public class AccountController {
	
	@Autowired
	AccountService accountService;
	
	@PostMapping("/deposit")
	public TransactionResponse depositAmount(@RequestBody TransactionRequest transactionReq) {
		return accountService.depositAmount(transactionReq);
	}
	
	@PostMapping("/withdraw")
	public TransactionResponse withdrawAmount(@RequestBody TransactionRequest transactionReq) {
		return accountService.withdrawAmount(transactionReq);
	}
	
	@PostMapping("/transfer")
	public TransactionResponse transferAmount(@RequestBody TransactionRequest transactionReq) {
		return accountService.transferAmount(transactionReq);
	}
	
	@GetMapping("/transactions/{accountId}")
	public List<TransactionResponse> getTransactionsList(@PathVariable Integer accountId){
		return accountService.getTransactionsList(accountId);
	}
	
	@PutMapping("/update-accountstatus")
	public AccountResponse updateAccountStatus(@RequestParam Integer customerId,@RequestParam String status) {
		return accountService.updateAccountStatus(customerId,status);
	}
	
	@GetMapping("/getaccount/{customerId}")
	public AccountResponse getAccount(@PathVariable Integer customerId) {
		return accountService.getAccount(customerId);
	}

}

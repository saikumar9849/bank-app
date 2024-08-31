package com.tarun.admin_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.tarun.admin_service.customer.dto.AccountResponse;
import com.tarun.admin_service.customer.dto.CustomerRequestDTO;
import com.tarun.admin_service.customer.dto.CustomerResponse;
import com.tarun.admin_service.customer.dto.LoanAccount;

@FeignClient("CUSTOMER-SERVICE")
public interface CustomerInterface {
	
	@PutMapping("/customers/update/{customerId}")
	public CustomerResponse updateCustomer(@PathVariable Integer customerId, @RequestBody CustomerRequestDTO customerReq);
	
	@GetMapping("/customers/get/{customerId}")
	public CustomerResponse getCustomer(@PathVariable Integer customerId);
	
	@GetMapping("/accounts/getaccount/{customerId}")
	public AccountResponse getAccount(@PathVariable Integer customerId);
	
	@PutMapping("/accounts/update-accountstatus")
	public AccountResponse updateAccountStatus(@RequestParam Integer customerId,@RequestParam String status);
	
//	@GetMapping("/transactions/{accountId}")
//	public List<TransactionResponse> getTransactionsList(@PathVariable Integer accountId)
	
	@PutMapping("/loans/loanapproval")
	public LoanAccount loanApproval(@RequestParam Integer customerId,@RequestParam String status);
	
	@GetMapping("/loans/loanDeatils/{customerId}")
	public LoanAccount loanDeatils(@PathVariable Integer customerId);

}

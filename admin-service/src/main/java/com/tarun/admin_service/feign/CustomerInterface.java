package com.tarun.admin_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.tarun.admin_service.customer.dto.AccountResponse;
import com.tarun.admin_service.customer.dto.CustomerRequestDTO;
import com.tarun.admin_service.customer.dto.CustomerResponse;
import com.tarun.admin_service.customer.dto.LoanAccount;
import com.tarun.admin_service.dto.LoginRequest;

@FeignClient("CUSTOMER-SERVICE")
public interface CustomerInterface {
	
	@PutMapping("/customers/admin/update/{customerId}")
	public ResponseEntity<CustomerResponse> updateCustomer(@RequestParam String token, @PathVariable Integer customerId, @RequestBody CustomerRequestDTO customerReq);
	
	@GetMapping("/customers/admin/get/{customerId}")
	public ResponseEntity<CustomerResponse> getCustomer(@RequestParam String token, @PathVariable Integer customerId);
	
	@GetMapping("/accounts/admin/getaccount/{customerId}")
	public ResponseEntity<AccountResponse> getAccount(@RequestParam String token, @PathVariable Integer customerId);
	
	@PutMapping("/accounts/admin/update-accountstatus")
	public ResponseEntity<AccountResponse> updateAccountStatus(@RequestParam String token, @RequestParam Integer customerId,@RequestParam String status);
	
	@PutMapping("/loans/admin/loanapproval")
	public ResponseEntity<LoanAccount> loanApproval(@RequestParam String token, @RequestParam Integer customerId,@RequestParam String status);
	
	@GetMapping("/loans/admin/loanDeatils/{customerId}")
	public ResponseEntity<LoanAccount> getLoanDeatils(@RequestParam String token, @PathVariable Integer customerId);
	
	@PostMapping("/customer-auth/get-token")
    public ResponseEntity<String> getToken(@RequestBody CustomerRequestDTO signUpRequest);

}

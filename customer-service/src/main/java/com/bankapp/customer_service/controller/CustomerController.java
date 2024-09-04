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

import com.bankapp.customer_service.dto.AccountResponse;
import com.bankapp.customer_service.dto.CustomerRequestDTO;
import com.bankapp.customer_service.dto.CustomerResponse;
import com.bankapp.customer_service.security.jwt.JwtUtils;
import com.bankapp.customer_service.service.CustomerService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/customers")
public class CustomerController {
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
    JwtUtils jwtUtils;
	
	@PutMapping("/update/{customerId}")
	@SecurityRequirement(name = "Bearer Authentication")
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Integer customerId, @RequestBody CustomerRequestDTO customerReq) {
		return customerService.updateCustomer(customerId, customerReq);
	}
	
	@GetMapping("/get/{customerId}")
	@SecurityRequirement(name = "Bearer Authentication")
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<CustomerResponse> getCustomer(@PathVariable Integer customerId) {
		return customerService.getCustomer(customerId);
	}
	
	@PostMapping("/create-account/{customerId}")
	@SecurityRequirement(name = "Bearer Authentication")
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<AccountResponse> createAccount(@PathVariable Integer customerId) {
		return customerService.createAccount(customerId);
	}
	
	@Hidden
	@PutMapping("/admin/update/{customerId}")
	public ResponseEntity<CustomerResponse> updateCustomer(@RequestParam String token, @PathVariable Integer customerId, @RequestBody CustomerRequestDTO customerReq) {
		if(jwtUtils.validateJwtToken(token)) {
			return customerService.updateCustomer(customerId, customerReq);
		}
		return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);
	}
	
	@Hidden
	@GetMapping("/admin/get/{customerId}")
	public ResponseEntity<CustomerResponse> getCustomer(@RequestParam String token, @PathVariable Integer customerId) {
		if(jwtUtils.validateJwtToken(token)) {
			return customerService.getCustomer(customerId);
		}
		return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);
	}

}

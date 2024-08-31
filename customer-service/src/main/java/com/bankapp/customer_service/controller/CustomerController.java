package com.bankapp.customer_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankapp.customer_service.dto.AccountResponse;
import com.bankapp.customer_service.dto.CustomerRequestDTO;
import com.bankapp.customer_service.dto.CustomerResponse;
import com.bankapp.customer_service.service.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {
	
	@Autowired
	CustomerService customerService;
	
	@PostMapping("/register")
	public CustomerResponse createCustomer(@RequestBody CustomerRequestDTO customerReq) {
		return customerService.createCustomer(customerReq);
	}
	
	@PutMapping("/update/{customerId}")
	public CustomerResponse updateCustomer(@PathVariable Integer customerId, @RequestBody CustomerRequestDTO customerReq) {
		return customerService.updateCustomer(customerId, customerReq);
	}
	
	@GetMapping("/get/{customerId}")
	public CustomerResponse getCustomer(@PathVariable Integer customerId) {
		return customerService.getCustomer(customerId);
	}
	
	@PostMapping("/create-account/{customerId}")
	public AccountResponse createAccount(@PathVariable Integer customerId) {
		return customerService.createAccount(customerId);
	}

}

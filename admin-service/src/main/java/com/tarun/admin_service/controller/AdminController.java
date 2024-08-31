package com.tarun.admin_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tarun.admin_service.customer.dto.AccountResponse;
import com.tarun.admin_service.customer.dto.CustomerRequestDTO;
import com.tarun.admin_service.customer.dto.CustomerResponse;
import com.tarun.admin_service.customer.dto.LoanAccount;
import com.tarun.admin_service.dto.AdminResponse;
import com.tarun.admin_service.dto.SignupRequest;
import com.tarun.admin_service.services.AdminService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/admins")
public class AdminController {
	
	@Autowired
	AdminService adminService;
	
	@GetMapping("/admin/{id}")
	@SecurityRequirement(name = "Bearer Authentication")
	public AdminResponse getAdminDetails(@PathVariable Integer id) {
		return adminService.getAdminDetails(id);
	}
	
	@PutMapping("/update/{id}")
	@SecurityRequirement(name = "Bearer Authentication")
	public AdminResponse getAdminDetails(@PathVariable Integer id, @RequestBody SignupRequest req) {
		return adminService.updateAdminDetails(id,req);
	}
	
	@PutMapping("/update-customer/{customerId}")
	@SecurityRequirement(name = "Bearer Authentication")
	public CustomerResponse updateCustomerData(@PathVariable Integer id, @RequestBody CustomerRequestDTO req) {
		return adminService.updateCustomerData(id,req);
	}
	
	@PutMapping("/customer-loanapproval")
	@SecurityRequirement(name = "Bearer Authentication")
	public LoanAccount loanApproval(@RequestParam Integer customerId) {
		return adminService.loanApproval(customerId);
	}
	
	@PutMapping("/update-accountstatus")
	@SecurityRequirement(name = "Bearer Authentication")
	public AccountResponse updateAccountStatus(@RequestParam Integer customerId) {
		return adminService.updateAccountStatus(customerId);
	}

}

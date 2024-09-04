package com.bankapp.admin_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bankapp.admin_service.customer.dto.AccountResponse;
import com.bankapp.admin_service.customer.dto.CustomerRequestDTO;
import com.bankapp.admin_service.customer.dto.CustomerResponse;
import com.bankapp.admin_service.customer.dto.LoanAccount;
import com.bankapp.admin_service.dto.AdminResponse;
import com.bankapp.admin_service.dto.SignupRequest;
import com.bankapp.admin_service.services.AdminService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/admins")
public class AdminController {
	
	@Autowired
	AdminService adminService;
	
	@GetMapping("/admin/{id}")
	@SecurityRequirement(name = "Bearer Authentication")
	public ResponseEntity<AdminResponse> getAdminDetails(@PathVariable Integer id) {
		return adminService.getAdminDetails(id);
	}
	
	@PutMapping("/update/{id}")
	@SecurityRequirement(name = "Bearer Authentication")
	public ResponseEntity<AdminResponse> updateAdminDetails(@PathVariable Integer id, @RequestBody SignupRequest req) {
		return adminService.updateAdminDetails(id,req);
	}
	
	@PutMapping("/update-customer")
	@SecurityRequirement(name = "Bearer Authentication")
	public ResponseEntity<CustomerResponse> updateCustomerData(@RequestParam Integer customerId, @RequestParam Integer adminId, @RequestBody CustomerRequestDTO req) {
		return adminService.updateCustomerData(customerId, adminId, req);
	}
	
	@PutMapping("/customer-loanapproval")
	@SecurityRequirement(name = "Bearer Authentication")
	public ResponseEntity<LoanAccount> loanApproval(@RequestParam Integer customerId, @RequestParam Integer adminId) {
		return adminService.loanApproval(customerId, adminId);
	}
	
	@PutMapping("/update-accountstatus")
	@SecurityRequirement(name = "Bearer Authentication")
	public ResponseEntity<AccountResponse> updateAccountStatus(@RequestParam Integer customerId, @RequestParam Integer adminId) {
		return adminService.updateAccountStatus(customerId, adminId);
	}

}

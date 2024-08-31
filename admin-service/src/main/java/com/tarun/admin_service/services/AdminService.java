package com.tarun.admin_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tarun.admin_service.customer.dto.AccountResponse;
import com.tarun.admin_service.customer.dto.CustomerRequestDTO;
import com.tarun.admin_service.customer.dto.CustomerResponse;
import com.tarun.admin_service.customer.dto.LoanAccount;
import com.tarun.admin_service.dto.AdminResponse;
import com.tarun.admin_service.dto.SignupRequest;
import com.tarun.admin_service.entity.Admin;
import com.tarun.admin_service.enumes.AccountStatus;
import com.tarun.admin_service.enumes.LoanStatus;
import com.tarun.admin_service.feign.CustomerInterface;
import com.tarun.admin_service.repository.AdminRepository;

@Service
public class AdminService {
	
	@Autowired
	AdminRepository adminRepo;
	
	@Autowired
    PasswordEncoder encoder;
	
	@Autowired
	CustomerInterface customerInterface;

	public AdminResponse getAdminDetails(Integer id) {
		Admin admin = adminRepo.findById(id).get();
		return convertAdminData(admin);
	}

	public AdminResponse updateAdminDetails(Integer id,SignupRequest req) {
		Admin admin = adminRepo.findById(id).get();
		admin.setContactNumber(req.getContactNumber());
		admin.setEmail(req.getEmail());
		admin.setFirstName(req.getFirstName());
		admin.setLastName(req.getLastName());
		admin.setLoginId(req.getLoginId());
		admin.setPassword(encoder.encode(req.getPassword()));
		Admin savedAdmin = adminRepo.save(admin);
		return convertAdminData(savedAdmin);
	}
	
	public CustomerResponse updateCustomerData(Integer id, CustomerRequestDTO req) {
		return customerInterface.updateCustomer(id, req);
	}
	
	private AdminResponse convertAdminData(Admin admin) {
		AdminResponse adminRes = new AdminResponse();
		adminRes.setId(admin.getId());
		adminRes.setFirstName(admin.getFirstName());
		adminRes.setLoginId(admin.getLoginId());
		adminRes.setLastName(admin.getLastName());
		adminRes.setContactNumber(admin.getContactNumber());
		adminRes.setEmail(admin.getEmail());
		return adminRes;
	}

	public LoanAccount loanApproval(Integer customerId) {
		CustomerResponse customer = customerInterface.getCustomer(customerId);
		if(customer.getLoanDetails()!=null && customer.getLoanDetails().getLoanStatus().equals(LoanStatus.INPROGRESS) && customer.getAccountDetails()!=null) {
			if(customer.getAccountDetails().getBalance()>=customer.getLoanDetails().getAmount()) {
				return customerInterface.loanApproval(customerId, LoanStatus.APPROVED.name());
			}
			return customerInterface.loanApproval(customerId, LoanStatus.REJECTED.name());
		}
		return null;
	}

	public AccountResponse updateAccountStatus(Integer customerId) {
		CustomerResponse customer = customerInterface.getCustomer(customerId);
		if(customer.getAccountDetails()!=null && customer.getAccountDetails().getBalance()<1000) {
			return customerInterface.updateAccountStatus(customerId, AccountStatus.SUSPENDED.name());
		}
		return customerInterface.getAccount(customerId);
	}

}

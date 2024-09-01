package com.tarun.admin_service.services;

import java.time.LocalDate;

import org.apache.coyote.http11.Http11InputBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.tarun.admin_service.enumes.ERole;
import com.tarun.admin_service.enumes.Gender;
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
	
	public ResponseEntity<CustomerResponse> updateCustomerData(Integer id, Integer adminId, CustomerRequestDTO req) {
		String token = getToken(adminId);
		return customerInterface.updateCustomer(token, id, req);
	}
	
	public ResponseEntity<LoanAccount> loanApproval(Integer customerId, Integer adminId) {
		String token = getToken(adminId);
		CustomerResponse customer = customerInterface.getCustomer(token, customerId).getBody();
		if(customer.getLoanDetails()!=null && customer.getLoanDetails().getLoanStatus().equals(LoanStatus.INPROGRESS) && customer.getAccountDetails()!=null) {
			if(customer.getAccountDetails().getBalance()>=customer.getLoanDetails().getAmount()) {
				return customerInterface.loanApproval(token, customerId, LoanStatus.APPROVED.name());
			}
		}
		return customerInterface.loanApproval(token, customerId, LoanStatus.REJECTED.name());
	}

	public ResponseEntity<AccountResponse> updateAccountStatus(Integer customerId, Integer adminId) {
		String token = getToken(adminId);
		CustomerResponse customer = customerInterface.getCustomer(token, customerId).getBody();
		if(customer==null) {
			return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);
		}
		if(customer.getAccountDetails()!=null && customer.getAccountDetails().getBalance()<1000) {
			return customerInterface.updateAccountStatus(token, customerId, AccountStatus.SUSPENDED.name());
		}
		return customerInterface.getAccount(token, customerId);
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
	
	private String getToken(Integer id) {
		Admin admin = adminRepo.findById(id).get();
		CustomerRequestDTO customer = new CustomerRequestDTO();
		customer.setFirstName(admin.getFirstName());
		customer.setLastName(admin.getLastName());
		customer.setNationality("Indian");
		customer.setEmail(admin.getEmail());
		customer.setPhoneNumber(Long.toString(admin.getContactNumber()));
		customer.setLoginId(admin.getLoginId());
		customer.setPassword(admin.getLoginId());
		customer.setDateOfBirth(LocalDate.now());
		customer.setPanNumber("NoPAN");
		customer.setGender(Gender.M);
		customer.setRole(ERole.ROLE_ADMIN);
		return customerInterface.getToken(customer).getBody();
	}

}

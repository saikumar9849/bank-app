package com.bankapp.customer_service.service;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bankapp.customer_service.dto.AccountResponse;
import com.bankapp.customer_service.dto.CustomerRequestDTO;
import com.bankapp.customer_service.dto.CustomerResponse;
import com.bankapp.customer_service.entities.Account;
import com.bankapp.customer_service.entities.Customer;
import com.bankapp.customer_service.enumes.AccountStatus;
import com.bankapp.customer_service.exception.CustomerNotFound;
import com.bankapp.customer_service.repository.AccountRepository;
import com.bankapp.customer_service.repository.CustomerRepository;

@Service
public class CustomerService {
	
	@Autowired
	CustomerRepository customerRepo;
	
	@Autowired
	AccountRepository accountRepo;
	
	@Autowired
	AccountService accountService;
	
	@Autowired
    PasswordEncoder encoder;

	public CustomerResponse createCustomer(CustomerRequestDTO customerReq) {
		Customer customer = setCustomerDetails(customerReq, new Customer());
		Customer savedCustomer = customerRepo.save(customer);
		return generateCustomerRes(savedCustomer);
	}

	public ResponseEntity<CustomerResponse> updateCustomer(Integer id, CustomerRequestDTO customerReq) {
		Customer customer = customerRepo.findById(id).get();
		if(customer==null) {
			throw new CustomerNotFound(String.format("Customer with id: %s not found", id));
		}
		Customer updatedCustomer = customerRepo.save(setCustomerDetails(customerReq, customer));
		return new ResponseEntity<CustomerResponse>(generateCustomerRes(updatedCustomer), HttpStatus.OK);
	}
	
	public ResponseEntity<CustomerResponse> getCustomer(Integer id) {
		Customer customer = customerRepo.findById(id).get();
		if(customer==null) {
			throw new CustomerNotFound(String.format("Customer with id: %s not found", id));
		}
		return new ResponseEntity<CustomerResponse>(generateCustomerRes(customer), HttpStatus.OK);
	}
	
	public ResponseEntity<AccountResponse> createAccount(Integer id) {
		Customer customer = customerRepo.findById(id).get();
		if(customer==null) {
			throw new CustomerNotFound(String.format("Customer with id: %s not found", id));
		}
		Account account = new Account();
		account.setBalance(0);
		account.setAccountStatus(AccountStatus.ACTIVATED);
		account.setTransactions(new ArrayList<>());
		account.setCreatedDate(LocalDate.now());
		Account savedAccount = accountRepo.save(account);
		customer.setAccount(account);
		customerRepo.save(customer);
		return new ResponseEntity<>(accountService.convertAccount(savedAccount), HttpStatus.OK);
	}
	
	private Customer setCustomerDetails(CustomerRequestDTO customerReq, Customer customer) {
		customer.setFirstName(customerReq.getFirstName());
		customer.setLastName(customerReq.getLastName());
		customer.setDateOfBirth(customerReq.getDateOfBirth());
		customer.setEmail(customerReq.getEmail());
		customer.setGender(customerReq.getGender());
		customer.setNationality(customerReq.getNationality());
		customer.setPanNumber(customerReq.getPanNumber());
		customer.setPhoneNumber(customerReq.getPhoneNumber());
		customer.setRole(customerReq.getRole());
		customer.setLoginId(customerReq.getLoginId());
		customer.setPassword(encoder.encode(customerReq.getPassword()));
		return customer;
	}
	
	private CustomerResponse generateCustomerRes(Customer c) {
		CustomerResponse res = new CustomerResponse();
		res.setId(c.getId());
		res.setFirstName(c.getFirstName());
		res.setLastName(c.getLastName());
		res.setDateOfBirth(c.getDateOfBirth());
		res.setEmail(c.getEmail());
		res.setGender(c.getGender());
		res.setLoanDetails(c.getLoan());
		res.setNationality(c.getNationality());
		res.setPanNumber(c.getPanNumber());
		res.setPhoneNumber(c.getPhoneNumber());
		res.setAccountDetails(accountService.convertAccount(c.getAccount()));
		return res;
	}

}

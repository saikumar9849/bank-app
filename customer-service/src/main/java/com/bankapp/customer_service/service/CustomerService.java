package com.bankapp.customer_service.service;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bankapp.customer_service.dto.AccountResponse;
import com.bankapp.customer_service.dto.CustomerRequestDTO;
import com.bankapp.customer_service.dto.CustomerResponse;
import com.bankapp.customer_service.entities.Account;
import com.bankapp.customer_service.entities.Customer;
import com.bankapp.customer_service.enumes.AccountStatus;
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

	public CustomerResponse createCustomer(CustomerRequestDTO customerReq) {
		Customer customer = setCustomerDetails(customerReq, new Customer());
		Customer savedCustomer = customerRepo.save(customer);
		return generateCustomerRes(savedCustomer);
	}

	public CustomerResponse updateCustomer(Integer id, CustomerRequestDTO customerReq) {
		Customer customer = customerRepo.findById(id).get();
		Customer updatedCustomer = customerRepo.save(setCustomerDetails(customerReq, customer));
		return generateCustomerRes(updatedCustomer);
	}
	
	public CustomerResponse getCustomer(Integer id) {
		return generateCustomerRes(customerRepo.findById(id).get());
	}

	public AccountResponse createAccount(Integer id) {
		Customer customer = customerRepo.findById(id).get();
		Account account = new Account();
		account.setBalance(0);
		account.setAccountStatus(AccountStatus.ACTIVATED);
		account.setTransactions(new ArrayList<>());
		account.setCreatedDate(LocalDate.now());
		Account savedAccount = accountRepo.save(account);
		customer.setAccount(account);
		customerRepo.save(customer);
		return accountService.convertAccount(savedAccount);
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

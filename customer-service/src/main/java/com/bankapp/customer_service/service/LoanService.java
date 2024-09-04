package com.bankapp.customer_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bankapp.customer_service.dto.LoanCalculatorResponse;
import com.bankapp.customer_service.dto.LoanRequest;
import com.bankapp.customer_service.entities.Customer;
import com.bankapp.customer_service.entities.LoanAccount;
import com.bankapp.customer_service.enumes.LoanStatus;
import com.bankapp.customer_service.exception.CustomerNotFound;
import com.bankapp.customer_service.repository.CustomerRepository;
import com.bankapp.customer_service.repository.LoanRepository;

@Service
public class LoanService {
	
	@Autowired
	LoanRepository loanRepo;
	
	@Autowired
	CustomerRepository customerRepo;
	
	public ResponseEntity<LoanCalculatorResponse> emiCalculator(LoanRequest loanReq) {
		LoanCalculatorResponse res = new LoanCalculatorResponse();
		res.setEmi(calculateLoan(loanReq));
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	public ResponseEntity<LoanAccount> applyLoan(Integer id, LoanRequest loanRequest) {
		LoanAccount acc = new LoanAccount();
		Customer customer = customerRepo.findById(id).get();
		if(customer==null) {
			throw new CustomerNotFound(String.format("Customer with id: %s not found", id));
		}
		acc.setAmount(loanRequest.getAmount());
		acc.setRateOfInterest(loanRequest.getRateOfInterest());
		acc.setTenureInMonths(loanRequest.getTenureInMonths());
		acc.setLoanStatus(LoanStatus.INPROGRESS);
		acc.setEmi(calculateLoan(loanRequest));
		LoanAccount savedLoan = loanRepo.save(acc);
		customer.setLoan(savedLoan);
		customerRepo.save(customer);
		return new ResponseEntity<>(savedLoan, HttpStatus.OK);
	}
	
	private Integer calculateLoan(LoanRequest loanAcc) {
		Integer amount = loanAcc.getAmount();
		Double rateOfInterest = (double)loanAcc.getRateOfInterest()/12;
		Integer tenure = loanAcc.getTenureInMonths();
		Integer emi = (int) (amount*rateOfInterest*(Math.pow(1+rateOfInterest,tenure)/(Math.pow(1+rateOfInterest,tenure)-1)));
		return emi;
	}

	public ResponseEntity<LoanAccount> loanApproval(Integer id, String status) {
		LoanStatus loanStatus = LoanStatus.valueOf(status);
		Customer customer = customerRepo.findById(id).get();
		if(customer==null) {
			throw new CustomerNotFound(String.format("Customer with id: %s not found", id));
		}
		LoanAccount loanAcc = customer.getLoan();
		loanAcc.setLoanStatus(loanStatus);
		LoanAccount savedLoan = loanRepo.save(loanAcc);
		customer.setLoan(savedLoan);
		customerRepo.save(customer);
		return new ResponseEntity<>(savedLoan, HttpStatus.OK);
	}

	public ResponseEntity<LoanAccount> getLoanDetails(Integer id) {
		Customer customer = customerRepo.findById(id).get();
		if(customer==null) {
			throw new CustomerNotFound(String.format("Customer with id: %s not found", id));
		}
		LoanAccount loanAcc = customer.getLoan();
		return new ResponseEntity<>(loanAcc, HttpStatus.OK);
	}

}

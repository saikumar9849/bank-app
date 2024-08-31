package com.bankapp.customer_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bankapp.customer_service.dto.LoanCalculatorResponse;
import com.bankapp.customer_service.dto.LoanRequest;
import com.bankapp.customer_service.entities.Customer;
import com.bankapp.customer_service.entities.LoanAccount;
import com.bankapp.customer_service.enumes.LoanStatus;
import com.bankapp.customer_service.repository.CustomerRepository;
import com.bankapp.customer_service.repository.LoanRepository;

@Service
public class LoanService {
	
	@Autowired
	LoanRepository loanRepo;
	
	@Autowired
	CustomerRepository customerRepo;
	
	public LoanCalculatorResponse emiCalculator(LoanRequest loanReq) {
		LoanCalculatorResponse res = new LoanCalculatorResponse();
		res.setEmi(calculateLoan(loanReq));
		return res;
	}
	
	public LoanAccount applyLoan(Integer id, LoanRequest loanRequest) {
		LoanAccount acc = new LoanAccount();
		Customer customer = customerRepo.findById(id).get();
		acc.setAmount(loanRequest.getAmount());
		acc.setRateOfInterest(loanRequest.getRateOfInterest());
		acc.setTenureInMonths(loanRequest.getTenureInMonths());
		acc.setLoanStatus(LoanStatus.INPROGRESS);
		acc.setEmi(calculateLoan(loanRequest));
		LoanAccount savedLoan = loanRepo.save(acc);
		customer.setLoan(savedLoan);
		customerRepo.save(customer);
		return savedLoan;
	}
	
	private Integer calculateLoan(LoanRequest loanAcc) {
		Integer amount = loanAcc.getAmount();
		Double rateOfInterest = (double)loanAcc.getRateOfInterest()/12;
		Integer tenure = loanAcc.getTenureInMonths();
		Integer emi = (int) (amount*rateOfInterest*(Math.pow(1+rateOfInterest,tenure)/(Math.pow(1+rateOfInterest,tenure)-1)));
		return emi;
	}

	public LoanAccount loanApproval(Integer id, String status) {
		LoanStatus loanStatus = LoanStatus.valueOf(status);
		Customer customer = customerRepo.findById(id).get();
		LoanAccount loanAcc = customer.getLoan();
		loanAcc.setLoanStatus(loanStatus);
		LoanAccount savedLoan = loanRepo.save(loanAcc);
		customer.setLoan(savedLoan);
		customerRepo.save(customer);
		return savedLoan;
	}

	public LoanAccount getLoanDetails(Integer id) {
		Customer customer = customerRepo.findById(id).get();
		LoanAccount loanAcc = customer.getLoan();
		return loanAcc;
	}

}

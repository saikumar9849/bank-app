package com.bankapp.customer_service.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bankapp.customer_service.dto.AccountResponse;
import com.bankapp.customer_service.dto.TransactionRequest;
import com.bankapp.customer_service.dto.TransactionResponse;
import com.bankapp.customer_service.entities.Account;
import com.bankapp.customer_service.entities.Customer;
import com.bankapp.customer_service.entities.Transactions;
import com.bankapp.customer_service.enumes.AccountStatus;
import com.bankapp.customer_service.enumes.TransactionType;
import com.bankapp.customer_service.repository.AccountRepository;
import com.bankapp.customer_service.repository.CustomerRepository;
import com.bankapp.customer_service.repository.TransactionRepository;

import jakarta.transaction.Transactional;

@Service
public class AccountService {
	
	@Autowired
	AccountRepository accountRepo;
	
	@Autowired
	TransactionRepository transactionRepo;
	
	@Autowired
	CustomerRepository customerRepo;

	@Transactional
	public TransactionResponse depositAmount(TransactionRequest transactionReq) {
		Account account = accountRepo.findById(transactionReq.getFrom()).get();
		Transactions transaction = new Transactions();
		transaction.setType(TransactionType.DEPOSIT);
		transaction.setAmount(transactionReq.getAmount());
		transaction.setFrom(account);
		transaction.setTo(null);
		transaction.setDateTime(LocalDateTime.now());
		Transactions savedTransaction = transactionRepo.save(transaction);
		List<Transactions> transactions = account.getTransactions();
		transactions.add(savedTransaction);
		account.setTransactions(transactions);
		account.setBalance(account.getBalance()+transactionReq.getAmount());
		accountRepo.save(account);
		return convertTransaction(savedTransaction);
	}
	
	@Transactional
	public TransactionResponse withdrawAmount(TransactionRequest transactionReq) {
		Account account = accountRepo.findById(transactionReq.getFrom()).get();
		Transactions transaction = new Transactions();
		transaction.setType(TransactionType.WITHDRAW);
		transaction.setAmount(transactionReq.getAmount());
		transaction.setFrom(account);
		transaction.setTo(null);
		transaction.setDateTime(LocalDateTime.now());
		Transactions savedTransaction = transactionRepo.save(transaction);
		List<Transactions> transactions = account.getTransactions();
		transactions.add(savedTransaction);
		account.setTransactions(transactions);
		account.setBalance(account.getBalance()+transactionReq.getAmount());
		accountRepo.save(account);
		return convertTransaction(savedTransaction);
	}
	
	@Transactional
	public TransactionResponse transferAmount(TransactionRequest transactionReq) {
		Account fromAccount = accountRepo.findById(transactionReq.getFrom()).get();
		Account toAccount = transactionReq.getTo() != null ? accountRepo.findById(transactionReq.getTo()).get() : null;
		Transactions transaction1 = new Transactions();
		transaction1.setType(TransactionType.TRANSFER);
		transaction1.setAmount(transactionReq.getAmount());
		transaction1.setFrom(fromAccount);
		transaction1.setTo(toAccount);
		transaction1.setDateTime(LocalDateTime.now());
		Transactions savedTransaction = transactionRepo.save(transaction1);
		
		List<Transactions> transactions1 = fromAccount.getTransactions();
		transactions1.add(savedTransaction);
		fromAccount.setTransactions(transactions1);
		fromAccount.setBalance(fromAccount.getBalance()-transactionReq.getAmount());
		
		List<Transactions> transactions2 = toAccount.getTransactions();
		transactions2.add(savedTransaction);
		toAccount.setTransactions(transactions2);
		toAccount.setTransactions(transactions2);
		toAccount.setBalance(toAccount.getBalance()+transactionReq.getAmount());
		
		accountRepo.save(fromAccount);
		accountRepo.save(toAccount);
		return convertTransaction(savedTransaction);
	}
	
	public List<TransactionResponse> getTransactionsList(Integer id) {
		return convertTransactions(accountRepo.findById(id).get().getTransactions());
	}
	
	private List<TransactionResponse> convertTransactions(List<Transactions> transcations){
		List<TransactionResponse>  transactionresponses = new ArrayList<>();
		for(Transactions transac: transcations) {
			transactionresponses.add(convertTransaction(transac));
		}
		return transactionresponses;
	}
	
	private TransactionResponse convertTransaction(Transactions transac) {
		if(transac==null) {
			return null;
		}
		TransactionResponse transacRes = new TransactionResponse();
		transacRes.setId(transac.getId());
		transacRes.setDateTime(transac.getDateTime());
		transacRes.setType(transac.getType());
		transacRes.setAmount(transac.getAmount());
		if(transac.getTo()!=null) {
			transacRes.setToAccount(transac.getTo().getId());
		}
		transacRes.setFromAccount(transac.getFrom().getId());
		return transacRes;
	}
	
	public AccountResponse convertAccount(Account ac) {
		if(ac==null) {
			return null;
		}
		AccountResponse accountRes = new AccountResponse();
		accountRes.setId(ac.getId());
		accountRes.setAccountStatus(ac.getAccountStatus());
		accountRes.setBalance(ac.getBalance());
		accountRes.setCreatedDate(ac.getCreatedDate());
		return accountRes;
	}

	public AccountResponse updateAccountStatus(Integer customerId, String status) {
		AccountStatus accStatus = AccountStatus.valueOf(status);
		Customer customer = customerRepo.findById(customerId).get();
		Account acc = customer.getAccount();
		if(acc!=null) {
			acc.setAccountStatus(accStatus);
			Account savedAcc = accountRepo.save(acc);
			customerRepo.save(customer);
			return convertAccount(savedAcc);
		}
		return convertAccount(acc);
	}

	public AccountResponse getAccount(Integer customerId) {
		Customer customer = customerRepo.findById(customerId).get();
		if(customer.getAccount()!=null) {
			return convertAccount(customer.getAccount());
		}
		return null;
	}

}

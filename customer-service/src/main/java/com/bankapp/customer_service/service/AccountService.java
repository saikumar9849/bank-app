package com.bankapp.customer_service.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bankapp.customer_service.dto.AccountResponse;
import com.bankapp.customer_service.dto.InvestmentRequest;
import com.bankapp.customer_service.dto.NotificationRequest;
import com.bankapp.customer_service.dto.TransactionRequest;
import com.bankapp.customer_service.dto.TransactionResponse;
import com.bankapp.customer_service.entities.Account;
import com.bankapp.customer_service.entities.Customer;
import com.bankapp.customer_service.entities.InvestmentAccount;
import com.bankapp.customer_service.entities.LoanAccount;
import com.bankapp.customer_service.entities.Transactions;
import com.bankapp.customer_service.enumes.AccountStatus;
import com.bankapp.customer_service.enumes.InvestmentType;
import com.bankapp.customer_service.enumes.TransactionType;
import com.bankapp.customer_service.exception.AccountNotFound;
import com.bankapp.customer_service.exception.CustomerNotFound;
import com.bankapp.customer_service.exception.LowBalance;
import com.bankapp.customer_service.feign.NotificationInterface;
import com.bankapp.customer_service.repository.AccountRepository;
import com.bankapp.customer_service.repository.CustomerRepository;
import com.bankapp.customer_service.repository.InvestmentRepository;
import com.bankapp.customer_service.repository.LoanRepository;
import com.bankapp.customer_service.repository.TransactionRepository;

import jakarta.transaction.Transactional;

@Service
public class AccountService {
	
	@Value("${admin.app.email}")
    private String email;
	
	@Autowired
	private AccountRepository accountRepo;
	
	@Autowired
	private TransactionRepository transactionRepo;
	
	@Autowired
	private CustomerRepository customerRepo;
	
	@Autowired
	private NotificationInterface notificationInterface;
	
	@Autowired
	private LoanRepository loanRepo;
	
	@Autowired
	private InvestmentRepository investmentRepo;

	@Transactional
	public ResponseEntity<TransactionResponse> depositAmount(TransactionRequest transactionReq) {
		Account account = accountRepo.findById(transactionReq.getFrom()).get();
		if(account==null) {
			throw new AccountNotFound(String.format("Account with id: %s not found", transactionReq.getFrom()));
		}
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
		return new ResponseEntity<>(convertTransaction(savedTransaction), HttpStatus.OK);
	}
	
	@Transactional
	public ResponseEntity<TransactionResponse> withdrawAmount(TransactionRequest transactionReq) {
		Account account = accountRepo.findById(transactionReq.getFrom()).get();
		if(account==null) {
			throw new AccountNotFound(String.format("Account with id: %s not found", transactionReq.getFrom()));
		}
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
		if((account.getBalance()-transactionReq.getAmount())<0) {
			throw new LowBalance("Account Balance is low cannot withdraw money");
		}
		account.setBalance(account.getBalance()-transactionReq.getAmount());
		Account savedAcc = accountRepo.save(account);
		if(savedAcc.getBalance()<=0) {
			sendNotification(savedAcc.getId());
		}
		return new ResponseEntity<>(convertTransaction(savedTransaction), HttpStatus.OK);
	}
	
	@Transactional
	public ResponseEntity<TransactionResponse> transferAmount(TransactionRequest transactionReq) {
		Account fromAccount = accountRepo.findById(transactionReq.getFrom()).get();
		Account toAccount = transactionReq.getTo() != null ? accountRepo.findById(transactionReq.getTo()).get() : null;
		if(fromAccount==null) {
			throw new AccountNotFound(String.format("Account with id: %s not found", transactionReq.getFrom()));
		}
		if(toAccount==null) {
			throw new AccountNotFound(String.format("Account with id: %s not found", transactionReq.getTo()));
		}
		Transactions transaction1 = new Transactions();
		transaction1.setType(TransactionType.TRANSFER);
		transaction1.setAmount(transactionReq.getAmount());
		transaction1.setFrom(fromAccount);
		transaction1.setTo(toAccount);
		transaction1.setDateTime(LocalDateTime.now());
		Transactions savedTransaction = transactionRepo.save(transaction1);
		
		List<Transactions> fromAccountTransactions = fromAccount.getTransactions();
		fromAccountTransactions.add(savedTransaction);
		fromAccount.setTransactions(fromAccountTransactions);
		if((fromAccount.getBalance()-transactionReq.getAmount())<0) {
			throw new LowBalance("Account Balance is low cannot transfer money");
		}
		fromAccount.setBalance(fromAccount.getBalance()-transactionReq.getAmount());
		
		//create new transaction for transaction2 also because the transaction id's should be unique
		Transactions transaction2 = new Transactions();
		transaction2.setType(TransactionType.TRANSFER);
		transaction2.setAmount(transactionReq.getAmount());
		transaction2.setFrom(fromAccount);
		transaction2.setTo(toAccount);
		transaction2.setDateTime(LocalDateTime.now());
		Transactions savedTransaction2 = transactionRepo.save(transaction2);
		
		List<Transactions> toAccountTransactions = toAccount.getTransactions();
		toAccountTransactions.add(savedTransaction2);
		toAccount.setTransactions(toAccountTransactions);
		toAccount.setBalance(toAccount.getBalance()+transactionReq.getAmount());
		
		Account savedAcc = accountRepo.save(fromAccount);
		accountRepo.save(toAccount);
		if(savedAcc.getBalance()<=0) {
			sendNotification(savedAcc.getId());
		}
		return new ResponseEntity<>(convertTransaction(savedTransaction), HttpStatus.OK);
	}
	
	@Transactional
	public ResponseEntity<TransactionResponse> investment(Integer customerId, InvestmentRequest investmentReq) {
		Customer customer = customerRepo.findById(customerId).get();
		if(customer==null) {
			throw new CustomerNotFound(String.format("Customer with id: %s not found", customerId));
		}
		
		Account account = customer.getAccount();
		if(account==null) {
			throw new CustomerNotFound("No account for this customer");
		}
		if((account.getBalance()-investmentReq.getAmount())<0) {
			throw new LowBalance("Account Balance is low cannot invest money");
		}
		
		if(investmentReq.getInvestmentType().equals(InvestmentType.LOAN)) {
			LoanAccount loan = customer.getLoan();
			if(loan == null) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}
			loan.setAmount(loan.getAmount()+investmentReq.getAmount());
			loanRepo.save(loan);
		}else if(investmentReq.getInvestmentType().equals(InvestmentType.FIXED_DEPOSITS)) {
			List<InvestmentAccount> investments = customer.getInvestmentAccount();
			InvestmentAccount investmentAccount = null;
			for(InvestmentAccount acc: investments) {
				if(acc.getInvestmentType().equals(InvestmentType.FIXED_DEPOSITS)) {
					investmentAccount = acc;
					break;
				}
			}
			if(investmentAccount==null) {
				investmentAccount = new InvestmentAccount();
				investmentAccount.setInvestmentType(InvestmentType.FIXED_DEPOSITS);
				investmentAccount.setInterestRate(7);
				investmentAccount.setBalance(investmentReq.getAmount());
			}else {
				investmentAccount.setBalance(investmentAccount.getBalance()+investmentReq.getAmount());
			}
			investmentRepo.save(investmentAccount);
		}else {
			List<InvestmentAccount> investments = customer.getInvestmentAccount();
			InvestmentAccount investmentAccount = null;
			for(InvestmentAccount acc: investments) {
				if(acc.getInvestmentType().equals(InvestmentType.RECURRING_DEPOSIT)) {
					investmentAccount = acc;
					break;
				}
			}
			if(investmentAccount==null) {
				investmentAccount = new InvestmentAccount();
				investmentAccount.setInvestmentType(InvestmentType.RECURRING_DEPOSIT);
				investmentAccount.setInterestRate(6);
				investmentAccount.setBalance(investmentReq.getAmount());
			}else {
				investmentAccount.setBalance(investmentAccount.getBalance()+investmentReq.getAmount());
			}
			investmentRepo.save(investmentAccount);
		}
		account.setBalance(account.getBalance()-investmentReq.getAmount());
		
		Transactions transaction = new Transactions();
		transaction.setAmount(investmentReq.getAmount());
		transaction.setDateTime(LocalDateTime.now());
		transaction.setFrom(account);
		transaction.setTo(null);
		transaction.setType(TransactionType.INVESTMENT);
		Transactions savedTransaction = transactionRepo.save(transaction);
		List<Transactions> transactions = account.getTransactions();
		transactions.add(savedTransaction);
		account.setTransactions(transactions);
		Account savedAcc = accountRepo.save(account);
		if(savedAcc.getBalance()<=0) {
			sendNotification(savedAcc.getId());
		}
		return new ResponseEntity<>(convertTransaction(savedTransaction), HttpStatus.OK);
	}
	
	public ResponseEntity<List<TransactionResponse>> getTransactionsList(Integer id) {
		Account account = accountRepo.findById(id).get();
		if(account==null) {
			throw new AccountNotFound(String.format("Account with id: %s not found", id));
		}
		return new ResponseEntity<>(convertTransactions(account.getTransactions()), HttpStatus.OK);
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
	
	private void sendNotification(Integer id) {
		final String body = String.format("The Balance for the customer with Account ID: %s "+" is LESS", Integer.toString(id));
		final String subject = "Low Balance Alert!";
		NotificationRequest notification = new NotificationRequest();
		notification.setTo(email);
		notification.setBody(body);
		notification.setSubject(subject);
		notificationInterface.send(notification);
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

	public ResponseEntity<AccountResponse> updateAccountStatus(Integer customerId, String status) {
		AccountStatus accStatus = AccountStatus.valueOf(status);
		Customer customer = customerRepo.findById(customerId).get();
		if(customer==null) {
			throw new CustomerNotFound(String.format("Customer with id: %s not found", customerId));
		}
		Account account = customer.getAccount();
		if(account==null) {
			throw new CustomerNotFound("No account for this customer");
		}
		account.setAccountStatus(accStatus);
		Account savedAcc = accountRepo.save(account);
		customerRepo.save(customer);
		return new ResponseEntity<>(convertAccount(savedAcc), HttpStatus.OK);
	}

	public ResponseEntity<AccountResponse> getAccount(Integer customerId) {
		Customer customer = customerRepo.findById(customerId).get();
		if(customer==null) {
			throw new CustomerNotFound(String.format("Customer with id: %s not found", customerId));
		}
		Account account = customer.getAccount();
		if(account==null) {
			throw new CustomerNotFound("No account for this customer");
		}
		return new ResponseEntity<>(convertAccount(account), HttpStatus.OK);
	}

}

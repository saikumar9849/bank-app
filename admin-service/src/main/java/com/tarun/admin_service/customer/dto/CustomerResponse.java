package com.tarun.admin_service.customer.dto;

import java.time.LocalDate;
import com.tarun.admin_service.enumes.Gender;

public class CustomerResponse {
	
	private Integer id;
	
	private String firstName;
	
	private String lastName;
	
	private LocalDate dateOfBirth;
	
	private String nationality;
	
	private Gender gender;
	
	private String email;
	
	private String phoneNumber;
	
	private String panNumber;
	
	private AccountResponse accountDetails;
	
	private LoanAccount loanDetails;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public AccountResponse getAccountDetails() {
		return accountDetails;
	}

	public void setAccountDetails(AccountResponse accountDetails) {
		this.accountDetails = accountDetails;
	}

	public LoanAccount getLoanDetails() {
		return loanDetails;
	}

	public void setLoanDetails(LoanAccount loanDetails) {
		this.loanDetails = loanDetails;
	}
}

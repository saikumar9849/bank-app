package com.bankapp.customer_service.entities;

import java.time.LocalDate;
import java.util.List;

import com.bankapp.customer_service.enumes.ERole;
import com.bankapp.customer_service.enumes.Gender;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String firstName;
	private String lastName;
	private LocalDate dateOfBirth;
	private String nationality;
	@Enumerated(EnumType.STRING)
	private Gender gender;
	private String email;
	private String phoneNumber;
	private String panNumber;
	private ERole role;
	private String loginId;
	private String password;

	@OneToOne
	private Account account;
	
	@OneToOne
	private LoanAccount loan;
	
	@OneToMany
	private List<InvestmentAccount> investmentAccount;
	
	public LoanAccount getLoan() {
		return loan;
	}
	public void setLoan(LoanAccount loan) {
		this.loan = loan;
	}
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
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public String getPanNumber() {
		return panNumber;
	}
	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}
	
	public ERole getRole() {
		return role;
	}
	public void setRole(ERole role) {
		this.role = role;
	}
	
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUsername() {
        return this.loginId;
    }
	public List<InvestmentAccount> getInvestmentAccount() {
		return investmentAccount;
	}
	public void setInvestmentAccount(List<InvestmentAccount> investmentAccount) {
		this.investmentAccount = investmentAccount;
	}

}

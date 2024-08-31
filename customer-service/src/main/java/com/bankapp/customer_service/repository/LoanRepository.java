package com.bankapp.customer_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bankapp.customer_service.entities.LoanAccount;

@Repository
public interface LoanRepository extends JpaRepository<LoanAccount, Integer> {

}

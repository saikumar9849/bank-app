package com.bankapp.customer_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bankapp.customer_service.entities.InvestmentAccount;

@Repository
public interface InvestmentRepository extends JpaRepository<InvestmentAccount, Integer> {

}

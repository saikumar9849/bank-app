package com.bankapp.customer_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bankapp.customer_service.entities.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {

}

package com.bankapp.customer_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bankapp.customer_service.entities.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	
	Optional<Customer> findByLoginId(String loginId);

    boolean existsByLoginId(String loginId);
    
    Boolean existsByEmail(String email);
}

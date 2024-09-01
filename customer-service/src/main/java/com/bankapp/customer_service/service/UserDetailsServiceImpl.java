package com.bankapp.customer_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bankapp.customer_service.entities.Customer;
import com.bankapp.customer_service.repository.CustomerRepository;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    CustomerRepository adminRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        Customer user = adminRepository.findByLoginId(username).get();

        return UserDetailsImpl.build(user);
    }

}

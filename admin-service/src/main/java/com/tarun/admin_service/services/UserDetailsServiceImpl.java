package com.tarun.admin_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tarun.admin_service.entity.Admin;
import com.tarun.admin_service.repository.AdminRepository;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    AdminRepository adminRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        Admin user = adminRepository.findByLoginId(username).get();

        return UserDetailsImpl.build(user);
    }

}

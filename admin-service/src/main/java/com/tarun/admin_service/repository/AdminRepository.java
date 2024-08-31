package com.tarun.admin_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tarun.admin_service.entity.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
	
	Optional<Admin> findByLoginId(String loginId);

    boolean existsByLoginId(String loginId);
    
    Boolean existsByEmail(String email);

}

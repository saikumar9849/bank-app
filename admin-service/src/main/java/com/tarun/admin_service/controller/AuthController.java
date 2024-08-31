package com.tarun.admin_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tarun.admin_service.dto.JwtResponse;
import com.tarun.admin_service.dto.LoginRequest;
import com.tarun.admin_service.dto.MessageResponse;
import com.tarun.admin_service.dto.SignupRequest;
import com.tarun.admin_service.entity.Admin;
import com.tarun.admin_service.enumes.ERole;
import com.tarun.admin_service.repository.AdminRepository;
import com.tarun.admin_service.security.jwt.JwtUtils;
import com.tarun.admin_service.services.UserDetailsImpl;

import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("/admin-auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AdminRepository userRepository;
    
    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    @Operation(summary = "login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getLoginId(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                ERole.ROLE_ADMIN));
    }

 

    @PostMapping("/register")
    @Operation(summary = "new registration")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByLoginId(signUpRequest.getLoginId())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: LoginId is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new Admin's account
        Admin admin = new Admin(signUpRequest.getLoginId(),
                signUpRequest.getFirstName(),
                signUpRequest.getLastName(),
                signUpRequest.getEmail(),
                signUpRequest.getContactNumber(),
                encoder.encode(signUpRequest.getPassword()));

        admin.setRole(ERole.ROLE_ADMIN);
        userRepository.save(admin);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        
    }
}

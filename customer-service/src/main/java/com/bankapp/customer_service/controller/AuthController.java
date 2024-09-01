package com.bankapp.customer_service.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankapp.customer_service.dto.CustomerRequestDTO;
import com.bankapp.customer_service.dto.JwtResponse;
import com.bankapp.customer_service.dto.LoginRequest;
import com.bankapp.customer_service.dto.MessageResponse;
import com.bankapp.customer_service.enumes.ERole;
import com.bankapp.customer_service.repository.CustomerRepository;
import com.bankapp.customer_service.security.jwt.JwtUtils;
import com.bankapp.customer_service.service.CustomerService;
import com.bankapp.customer_service.service.UserDetailsImpl;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/customer-auth")
public class AuthController {
	
	@Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomerRepository userRepository;
    
    @Autowired
    CustomerService customerService;

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
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                ERole.valueOf(roles.get(0))));
    }
    
    @PostMapping("/register")
    @Operation(summary = "new registration")
    public ResponseEntity<?> registerUser(@RequestBody CustomerRequestDTO signUpRequest) {
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

        // Create new Customer's account
        customerService.createCustomer(signUpRequest);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        
    }
    
    @PostMapping("/get-token")
    public ResponseEntity<String> getToken(@RequestBody CustomerRequestDTO signUpRequest) {
    	if(!userRepository.existsByLoginId(signUpRequest.getLoginId())) {
    		customerService.createCustomer(signUpRequest);
    	}
    	Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signUpRequest.getLoginId(), signUpRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        return ResponseEntity.ok(jwt);
    }

}

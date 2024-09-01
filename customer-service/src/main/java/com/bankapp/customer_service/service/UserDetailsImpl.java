package com.bankapp.customer_service.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.bankapp.customer_service.entities.Customer;
import com.bankapp.customer_service.enumes.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String loginId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
	private String panNumber;
	private LocalDate dateOfBirth;
	private String nationality;
	private Gender gender;

    @JsonIgnore
    private String password;
    
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Integer id,String loginId, String firstName, String lastName, String email, String phoneNumber, String password, LocalDate dateOfBirth, String nationality, Gender gender, String panNumber, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.loginId = loginId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.gender = gender;
        this.panNumber = panNumber;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.authorities = authorities;
    }


    public static UserDetailsImpl build(Customer user) {
//        List<GrantedAuthority> authorities = user.getRoles().stream()
//                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
//                .collect(Collectors.toList());
    	List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getPassword(),
                user.getDateOfBirth(),
                user.getNationality(),
                user.getGender(),
                user.getPanNumber(),
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }
    
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    @Override
    public String getUsername() {
        return loginId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}

package com.bankapp.admin_service.dto;

import com.bankapp.admin_service.enumes.ERole;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Integer id;

    private String loginId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private String email;
    private ERole role;

    public JwtResponse(String accessToken, Integer  id, String loginId, String email, ERole role) {
        this.token = accessToken;
        this.id = id;
        this.loginId = loginId;
        this.email = email;
        this.role = role;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return loginId;
    }

    public void setUsername(String loginId) {
        this.loginId = loginId;
    }

    public ERole getRole() {
        return role;
    }
}
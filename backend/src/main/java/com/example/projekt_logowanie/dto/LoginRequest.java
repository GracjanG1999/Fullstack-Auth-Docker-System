package com.example.projekt_logowanie.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}

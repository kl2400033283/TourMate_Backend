package com.klu.dto;

import lombok.Data;

@Data
public class SignupRequest {
    private String email;
    private String password;
}
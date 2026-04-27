package com.klu.dto;

public class LoginResponse {

    private Long id;
    private String token;
    private String role;
    private String email;
    private String fullName;
    private String city;
    private boolean googleUser;

    public LoginResponse(Long id, String token, String role, String email, String fullName, String city, boolean googleUser) {
        this.id = id;
        this.token = token;
        this.role = role;
        this.email = email;
        this.fullName = fullName;
        this.city = city;
        this.googleUser = googleUser;
    }

    public Long getId() { return id; }
    public String getToken() { return token; }
    public String getRole() { return role; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
    public String getCity() { return city; }
    public boolean isGoogleUser() { return googleUser; }
}

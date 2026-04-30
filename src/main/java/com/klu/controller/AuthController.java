package com.klu.controller;

import com.klu.dto.LoginRequest;
import com.klu.entity.User;
import com.klu.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "https://tour-mate-frontend.vercel.app") 
public class AuthController {

    @Autowired
    private AuthService service;

    //  SIGNUP
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        return ResponseEntity.ok(service.signup(user));
    }

    //  LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(
                service.login(request.getEmail(), request.getPassword())
        );
    }

    //  GOOGLE LOGIN
    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> data) {
        return ResponseEntity.ok(
            service.loginWithGoogle(data.get("email"), data.get("name"), data.get("role"))
        );
    }

    //  SEND OTP
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgot(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(service.sendOtpForReset(request.getEmail()));
    }

    //  VERIFY OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verify(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(
                service.verifyOtp(body.get("email"), body.get("otp"))
        );
    }

    //  RESET PASSWORD
    @PostMapping("/reset-password")
    public ResponseEntity<?> reset(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(
                service.resetPassword(body.get("email"), body.get("password"))
        );
    }
}
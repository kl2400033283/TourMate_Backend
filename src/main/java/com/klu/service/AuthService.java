package com.klu.service;

import com.klu.entity.User;
import com.klu.repository.UserRepository;
import com.klu.config.JwtUtil;
import com.klu.dto.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //  SIGNUP
    public String signup(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "User already exists";
        }

        if ("admin".equalsIgnoreCase(user.getRole())) {
            return "Invalid role";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setVerified(true);

        if ("guide".equalsIgnoreCase(user.getRole()) || "host".equalsIgnoreCase(user.getRole())) {
            user.setApproved(false);
        } else {
            user.setApproved(true);
        }

        if (user.getRole() == null) {
            user.setRole("tourist");
        }

        userRepository.save(user);
        return "Signup successful";
    }

    //  LOGIN
    public LoginResponse login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isGoogleUser()) {
            throw new RuntimeException("This account uses Google Sign-In. Please use 'Sign in with Google'.");
        }

        if (!user.isApproved()) {
            throw new RuntimeException("Waiting for admin approval");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(email);
        return new LoginResponse(user.getId(), token, user.getRole().toLowerCase(), user.getEmail(), user.getName(), user.getCity(), false);
    }

    //  GOOGLE LOGIN
    public LoginResponse loginWithGoogle(String email, String name, String role) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        User user;

        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setRole(role != null ? role : "tourist");
            user.setVerified(true);
            user.setGoogleUser(true);
            user.setApproved(!("guide".equalsIgnoreCase(role) || "host".equalsIgnoreCase(role)));
            userRepository.save(user);
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return new LoginResponse(user.getId(), token, user.getRole().toLowerCase(), user.getEmail(), user.getName(), user.getCity(), true);
    }

    //  SEND OTP
    public String sendOtpForReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        user.setOtp(otp);
        userRepository.save(user);

        try {
            emailService.sendOtp(email, otp);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage());
        }
        return "OTP sent to email";
    }

    //  VERIFY OTP
    public String verifyOtp(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getOtp() == null || !user.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }
        return "OTP verified";
    }

    //  RESET PASSWORD
    public String resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setOtp(null);

        userRepository.save(user);
        return "Password reset successful";
    }
}

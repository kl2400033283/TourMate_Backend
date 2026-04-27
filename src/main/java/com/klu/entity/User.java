package com.klu.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private String otp;
    private String role;

    private boolean approved;

    private String city;

    @Column(name = "google_user", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean googleUser;

    @Column(name = "is_verified", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean verified;
}

package com.klu.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String touristName;
    private String homestayName;
    private String guideName;
    private String cities;
    private String status;

    private LocalDate startDate;
    private LocalDate endDate;

    // email is not in DB — kept transient for frontend use only
    @Transient
    private String email;

    // Store tourist email for chat
    private String touristEmail;
}

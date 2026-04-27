package com.klu.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "guide")
public class Guide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String guideName;
    private String touristName;
    private String cities;
    private String status;

    private int totalBookings;
    private int accepted;
    private int rejected;

    @Column(columnDefinition = "TINYINT(1) DEFAULT 1")
    private boolean available = true;
}

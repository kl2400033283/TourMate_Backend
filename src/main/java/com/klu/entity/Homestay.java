package com.klu.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "homestay")
public class Homestay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String homestayName;
    private String hostName;
    private String hostEmail;
    private String city;
    private String touristName;
    private String status;
}

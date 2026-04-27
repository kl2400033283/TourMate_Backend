package com.klu.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "chat_message")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String chatKey;   // e.g. "tourist@email.com_guide_mysore"
    private String sender;    // "tourist", "guide", "host"
    private String text;
    private Long timestamp;
}

package com.klu.controller;

import com.klu.entity.ChatMessage;
import com.klu.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:5173")
public class ChatController {

    @Autowired
    private ChatMessageRepository repo;

    // Get all messages for a chat room
    @GetMapping("/{chatKey}")
    public List<ChatMessage> getMessages(@PathVariable String chatKey) {
        return repo.findByChatKeyOrderByTimestampAsc(chatKey);
    }

    // Send a message
    @PostMapping
    public ChatMessage sendMessage(@RequestBody ChatMessage message) {
        if (message.getTimestamp() == null) {
            message.setTimestamp(System.currentTimeMillis());
        }
        return repo.save(message);
    }
}

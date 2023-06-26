package com.chatgpt.app.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatGptRequest {

    private String model;
    private List<Message> messages;

    private boolean stream;
    public ChatGptRequest(String model, String prompt, boolean stream) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new Message("user", prompt));
        this.stream = stream;
    }
}

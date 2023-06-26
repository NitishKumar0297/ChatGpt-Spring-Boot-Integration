package com.chatgpt.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatGptResponse {

    private String id;

    private Long created;

    private List<Choice> choices;
}

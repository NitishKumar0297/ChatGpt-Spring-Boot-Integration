package com.chatgpt.app.dto;

import lombok.Data;

@Data
public class Choice {

    private int index;

    private Message message;

    private Delta delta;

}

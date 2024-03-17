package com.capstone.ChatService.Entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "message")
public class Message {
    @Id
    private Integer messageId;
    private Integer senderId;
    private Integer targetId;
    private String message;
    private String time;
}
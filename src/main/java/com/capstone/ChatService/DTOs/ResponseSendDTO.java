package com.capstone.ChatService.DTOs;

import com.capstone.ChatService.Entities.Message;

import java.util.List;

public class ResponseSendDTO {
    private Integer code;
    private Message data;
    private String message;

    public ResponseSendDTO(Integer code, Message data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Message getData() {
        return data;
    }

    public void setData(Message data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ResponseSendDTO{" +
                "code=" + code +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}

package com.capstone.ChatService.DTOs;

import com.capstone.ChatService.Entities.Message;

import java.util.List;

public class ResponseDataDTO {
    private Integer code;
    private List<Message> data;
    private String message;

    public ResponseDataDTO(Integer code, List<Message> data, String message) {
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

    public List<Message> getData() {
        return data;
    }

    public void setData(List<Message> data) {
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
        return "ResponseDataDTO{" +
                "code=" + code +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}

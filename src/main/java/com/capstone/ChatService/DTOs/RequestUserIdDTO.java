package com.capstone.ChatService.DTOs;

public class RequestUserIdDTO {
    private Integer senderId;
    private Integer targetId;

    public RequestUserIdDTO(Integer senderId, Integer targetId) {
        this.senderId = senderId;
        this.targetId = targetId;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    @Override
    public String toString() {
        return "RequestUserNameDTO{" +
                "senderId=" + senderId +
                ", targetId=" + targetId +
                '}';
    }
}

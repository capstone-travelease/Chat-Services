package com.capstone.ChatService.DTOs;

public class RequestUserNameDTO {
    private String senderName;
    private String targetName;

    public RequestUserNameDTO(String senderName, String targetName) {
        this.senderName = senderName;
        this.targetName = targetName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    @Override
    public String toString() {
        return "RequestUserNameDTO{" +
                "senderName='" + senderName + '\'' +
                ", targetName='" + targetName + '\'' +
                '}';
    }
}

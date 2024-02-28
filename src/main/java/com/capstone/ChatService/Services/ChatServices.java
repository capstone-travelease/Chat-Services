package com.capstone.ChatService.Services;

import com.capstone.ChatService.Entities.Message;
import com.capstone.ChatService.Repositories.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServices {
    @Autowired
    private ChatRepository chatRepository;

    public Integer saveChat(Message message){
        try{
            chatRepository.save(message);
            return 1;
        } catch (Exception e){
            return 2;
        }
    }

    public List<Message> getChat(String senderName, String targetName){
        return chatRepository.findBySenderAndTarget(senderName, targetName);
    }
}

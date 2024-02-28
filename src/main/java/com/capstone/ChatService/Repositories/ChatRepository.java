package com.capstone.ChatService.Repositories;

import com.capstone.ChatService.Entities.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends MongoRepository<Message, String> {
    @Query("{ 'senderName': ?0, 'targetUserName': ?1 }")
    List<Message> findBySenderAndTarget(String senderName, String targetUserName);
}

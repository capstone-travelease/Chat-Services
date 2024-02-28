package com.capstone.ChatService.Controllers;

import com.capstone.ChatService.DTOs.RequestUserNameDTO;
import com.capstone.ChatService.DTOs.ResponseDataDTO;
import com.capstone.ChatService.DTOs.ResponseStatusDTO;
import com.capstone.ChatService.Entities.Message;
import com.capstone.ChatService.Services.ChatServices;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Log4j2
public class ChatController {
    @Autowired
    private SocketIOServer socketServer;

    @Autowired
    private ChatServices chatServices;

    private Integer min = 1;
    private Integer max = 99999;

    ChatController(SocketIOServer socketServer){
        this.socketServer=socketServer;

        this.socketServer.addConnectListener(onUserConnectWithSocket);
        this.socketServer.addDisconnectListener(onUserDisconnectWithSocket);

//        this.socketServer.addEventListener("chatEvent", Message.class, onSendMessage);
    }

    @PostMapping("/sendChat")
    public ResponseEntity<ResponseStatusDTO> sendChatting(@RequestBody Message messagePost){
        try{
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule()); // Register the JavaTimeModule

            messagePost.setTime(LocalDateTime.now().toString());
            messagePost.setMessageId((int)(Math.random() * (max - min)) + min);

            socketServer.getBroadcastOperations().sendEvent(messagePost.getTargetUserName(), messagePost);

            var saveStatus = chatServices.saveChat(messagePost);
            if (saveStatus == 1){
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseStatusDTO(
                        200,
                        "Sent and Saved chat Successful",
                        "Successful"
                    )
                );
            } else{
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(
                        new ResponseStatusDTO(
                                200,
                                "Error in Saved Chat",
                                "Failure"
                        )
                );
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseStatusDTO(
                            404,
                            "Error in Sent Chat",
                            "Failure"
                    )
            );
        }
    }

    @GetMapping("/getChat")
    public ResponseEntity getSenderChatList(@RequestBody RequestUserNameDTO userName){
        var data = chatServices.getChat(userName);
        if (data.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseStatusDTO(
                            404,
                            "Not found chat with " + userName.getSenderName() +" and "+ userName.getTargetName(),
                            "Failure"
                    )
            );
        }
        else{
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseDataDTO(
                            200,
                            data,
                            "Successful"
                    )
            );
        }
    }

    public ConnectListener onUserConnectWithSocket = new ConnectListener() {
        @Override
        public void onConnect(SocketIOClient client) {
            log.info("Perform operation on user connect in controller");
        }
    };

    public DisconnectListener onUserDisconnectWithSocket = new DisconnectListener() {
        @Override
        public void onDisconnect(SocketIOClient client) {
            log.info("Perform operation on user disconnect in controller");
        }
    };

    public DataListener<Message> onSendMessage = new DataListener<Message>() {
        @Override
        public void onData(SocketIOClient client, Message message, AckRequest acknowledge) throws Exception {
            socketServer.getBroadcastOperations().sendEvent(message.getTargetUserName(),client, message);
            // After sending message to target user we can send acknowledge to sender
            acknowledge.sendAckData("Message send to target user successfully");
        }
    };
}

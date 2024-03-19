package com.capstone.ChatService.Controllers;

import com.capstone.ChatService.DTOs.RequestUserIdDTO;
import com.capstone.ChatService.DTOs.ResponseDataDTO;
import com.capstone.ChatService.DTOs.ResponseSendDTO;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

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

        this.socketServer.addEventListener("sendChat", Message.class, onSendMessage);
    }

    @PostMapping("/sendChat")
    public ResponseEntity<ResponseSendDTO> sendChatting(@RequestBody Message messagePost){
        try{
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule()); // Register the JavaTimeModule

            messagePost.setTime(LocalDateTime.now().toString());
            messagePost.setMessageId((int)(Math.random() * (max - min)) + min);

            socketServer.getBroadcastOperations().sendEvent("receiveChat", messagePost);

            var saveStatus = chatServices.saveChat(messagePost);
            if (saveStatus == 1){
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseSendDTO(
                        200,
                        messagePost,
                        "Successful"
                    )
                );
            } else{
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(
                        new ResponseSendDTO(
                                200,
                                messagePost,
                                "Failure"
                        )
                );
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseSendDTO(
                            404,
                            messagePost,
                            "Failure"
                    )
            );
        }
    }

    @GetMapping("/getChat")
    public ResponseEntity getSenderChatList(@RequestBody RequestUserIdDTO userId){
        var data = chatServices.getChat(userId);
        if (data.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseDataDTO(
                            200,
                            List.of(),
                            "Successful"
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
            socketServer.getBroadcastOperations().sendEvent("receiveChat",client, message);
            // After sending message to target user we can send acknowledge to sender
            acknowledge.sendAckData("Message send to target user successfully");
        }
    };
}

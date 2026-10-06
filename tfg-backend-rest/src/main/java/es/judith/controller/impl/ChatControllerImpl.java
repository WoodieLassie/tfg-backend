package es.judith.controller.impl;

import es.judith.bo.ChatMessageBO;
import es.judith.controller.ChatController;
import es.judith.domain.ChatMessage;
import es.judith.domain.ChatNotification;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class ChatControllerImpl implements ChatController {
    private final ChatMessageBO chatMessageBO;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatControllerImpl(ChatMessageBO chatMessageBO, SimpMessagingTemplate messagingTemplate) {
        this.chatMessageBO = chatMessageBO;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/{senderId}/{receiverId}")
    public ResponseEntity<List<ChatMessage>> getMessages(@PathVariable Long senderId, @PathVariable Long receiverId) {
        return ResponseEntity.ok(chatMessageBO.findChatMessages(senderId, receiverId));
    }

    @MessageMapping("/chat")
    public void processMessage(ChatMessage chatMessage) { //ChatMessageInputDTO chatMessageDTO
        //ChatMessage chatMessageToSave = chatMessageDTO.obtainDomainObject
        //Setear propiedades de chatMessage como en el resto de controllers
        //Solo posible si el usuario es una amistad. Inyectar friendBO
        ChatMessage chatMessageToSave = chatMessageBO.saveMessage(chatMessage);
        messagingTemplate.convertAndSendToUser(
                chatMessageToSave.getReceiverUser().getUsername(),
                "/queue/messages",
                ChatNotification.builder()
                        .chatRoomReference(chatMessageToSave.getChatRoom().getChatRoomReference())
                        .userSenderName(chatMessageToSave.getSenderUser().getUsername())
                        .userReceiverName(chatMessageToSave.getReceiverUser().getUsername())
                        .content(chatMessageToSave.getContent())
                        .build()
        );
    }
}

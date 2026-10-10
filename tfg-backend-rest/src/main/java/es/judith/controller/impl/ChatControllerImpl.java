package es.judith.controller.impl;

import es.judith.bo.ChatMessageBO;
import es.judith.bo.UserBO;
import es.judith.controller.ChatController;
import es.judith.domain.chat.ChatMessage;
import es.judith.domain.chat.ChatNotification;
import es.judith.domain.user.User;
import es.judith.dto.chat.ChatMessageDTO;
import es.judith.dto.chat.ChatMessageInputDTO;
import es.judith.dto.swagger.FriendSwaggerDTO;
import es.judith.exceptions.BadInputException;
import es.judith.exceptions.NotExistingIdException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/messages")
@Tag(name = "chats")
//TODO: Autenticacion?
public class ChatControllerImpl implements ChatController {

    @Serial
    private static final long serialVersionUID = -5184484261232203919L;
    private static final Logger LOG = LoggerFactory.getLogger(ChatControllerImpl.class);
    private final transient UserBO userBO;
    private final transient ChatMessageBO chatMessageBO;
    private final transient SimpMessagingTemplate messagingTemplate;

    public ChatControllerImpl(UserBO userBO, ChatMessageBO chatMessageBO, SimpMessagingTemplate messagingTemplate) {
        this.userBO = userBO;
        this.chatMessageBO = chatMessageBO;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/{senderId}/{receiverId}")
    @Operation(
            method = "GET",
            summary = "Get all messages between two users")
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ChatMessageDTO.class)))
            })
    @ApiResponse(
            responseCode = "403",
            description = "Forbidden",
            content = @Content(schema = @Schema(hidden = true)))
    public ResponseEntity<List<ChatMessageDTO>> getMessages(@PathVariable Long senderId, @PathVariable Long receiverId) {
        LOG.debug("ChatControllerImpl: Fetching all messages");
        List<ChatMessage> chatMessages = chatMessageBO.findChatMessages(senderId, receiverId);
        List<ChatMessageDTO> convertedChatMessages = new ArrayList<>();
        for (ChatMessage chatMessage : chatMessages) {
            convertedChatMessages.add(chatMessageBO.convertToDTO(chatMessage));
        }
        return ResponseEntity.ok(convertedChatMessages);
    }

    @MessageMapping("/chat")
    public void processMessage(ChatMessageInputDTO chatMessageDTO) {
        if (!chatMessageDTO.allFieldsArePresent()) {
            throw new BadInputException("All fields must be present in request body");
        }
        User userSender = userBO.findOne(chatMessageDTO.getUserSenderId());
        User userReceiver = userBO.findOne(chatMessageDTO.getUserReceiverId());
        if (userSender == null || userReceiver == null) {
            throw new NotExistingIdException(
                    "Invalid user IDs"
            );
        }
        ChatMessage chatMessageToSave = chatMessageDTO.obtainDomainObject();
        chatMessageToSave.setReceiverUser(userReceiver);
        chatMessageToSave.setSenderUser(userSender);
        //TODO: Solo posible si el usuario es una amistad. Inyectar friendBO
        LOG.debug("ChatControllerImpl: Processing new message");
        chatMessageBO.saveMessage(chatMessageToSave);
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

package es.judith.controller;

import es.judith.dto.chat.ChatMessageDTO;
import es.judith.dto.chat.ChatMessageInputDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

public interface ChatController extends Serializable {
    ResponseEntity<Page<ChatMessageDTO>> getMessages(Long senderId, Long receiverId, Integer page, Integer size, Pageable pageable);
    void processMessage(ChatMessageInputDTO chatMessageDTO);
}

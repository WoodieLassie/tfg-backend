package es.judith.bo;

import es.judith.domain.chat.ChatMessage;
import es.judith.dto.chat.ChatMessageDTO;

import java.util.List;

public interface ChatMessageBO extends GenericBO<ChatMessage, Long> {
    ChatMessage saveMessage(ChatMessage chatMessage);
    List<ChatMessage> findChatMessages (Long senderId, Long receiverId);
    ChatMessageDTO convertToDTO(ChatMessage chatMessage);
}

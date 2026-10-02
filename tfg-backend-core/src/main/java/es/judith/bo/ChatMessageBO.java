package es.judith.bo;

import es.judith.domain.ChatMessage;

import java.util.List;

public interface ChatMessageBO extends GenericBO<ChatMessage, Long> {
    ChatMessage saveMessage(ChatMessage chatMessage);
    List<ChatMessage> findChatMessages (Long senderId, Long receiverId);
}

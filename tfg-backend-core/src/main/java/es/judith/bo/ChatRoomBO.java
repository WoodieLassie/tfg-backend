package es.judith.bo;

import es.judith.domain.ChatRoom;
import es.judith.domain.User;

import java.util.Optional;

public interface ChatRoomBO extends GenericBO<ChatRoom, Long> {
    public Optional<String> getChatRoomReference(Long senderId, Long receiverId, boolean createNewRoomIfNotExists);
    String createChatRoomReference(User userSender, User userReceiver);
}

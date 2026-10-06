package es.judith.bo;

import es.judith.domain.chat.ChatRoom;
import es.judith.domain.user.User;

import java.util.Optional;

public interface ChatRoomBO extends GenericBO<ChatRoom, Long> {
    Optional<String> getChatRoomReference(Long senderId, Long receiverId, boolean createNewRoomIfNotExists);
    String createChatRoomReference(User userSender, User userReceiver);
    Optional<ChatRoom> findByChatRoomReference (String chatRoomReference);
}

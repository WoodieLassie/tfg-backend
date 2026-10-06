package es.judith.dao;

import es.judith.domain.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomRepository extends GenericRepository<ChatRoom, Long>, JpaSpecificationExecutor<ChatRoom> {
    @Query(
            value = "SELECT c.* from chat_rooms c WHERE c.sender_id = :senderId AND c.receiver_id = :receiverId",
            nativeQuery = true)
    Optional<ChatRoom> findBySenderIdAndReceiverId(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);
    @Query(
            value = "SELECT c.* from chat_rooms c WHERE c.chatRoomReference = :chatRoomReference",
            nativeQuery = true)
    Optional<ChatRoom> findByChatRoomReference(@Param("chatRoomReference") String chatRoomReference);
}

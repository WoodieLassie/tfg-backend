package es.judith.dao;

import es.judith.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends GenericRepository<ChatMessage, Long>, JpaSpecificationExecutor<ChatMessage> {
    @Query(
            value = "SELECT c.* from chat_messages c WHERE c.chatroom_id = :chatRoomId",
            nativeQuery = true)
    List<ChatMessage> findByChatRoomId(@Param("chatRoomId") Long chatRoomId);
}

package es.judith.dao;

import es.judith.domain.Friend;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendRepository extends GenericRepository<Friend, Long>, JpaSpecificationExecutor<Friend> {
    @Query(
            nativeQuery = true,
            value = "SELECT f.* FROM friendships f WHERE f.sender_id = :userId AND f.request_status = 1 OR f.receiver_id = :userId AND f.request_status = 1"
    )
    List<Friend> getAllFriends(@Param("userId") Long userId);
    @Query(
            nativeQuery = true,
            value = "SELECT f.* FROM friendships f WHERE f.sender_id = :userId AND f.request_status = 0"
    )
    List<Friend> getAllSentRequests(@Param("userId") Long userId);
    @Query(
            nativeQuery = true,
            value = "SELECT f.* FROM friendships f WHERE f.receiver_id = :userId AND f.request_status = 0"
    )
    List<Friend> getAllReceivedRequests(@Param("userId") Long userId);
    @Query(
            nativeQuery = true,
            value = "SELECT f.* FROM friendships f WHERE f.sender_id = :user1 AND f.receiver_id = :user2 OR f.sender_id = :user2 AND f.receiver_id = :user1"
    )
    Friend getBySenderAndReceiverId(@Param("user1") Long user1, @Param("user2") Long user2);
}

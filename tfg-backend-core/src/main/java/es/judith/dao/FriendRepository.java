package es.judith.dao;

import es.judith.domain.Friend;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendRepository extends ElvisBaseRepository<Friend, Long>, JpaSpecificationExecutor<Friend> {
    @Query(
            nativeQuery = true,
            value = "SELECT f.* FROM friendships f WHERE f.sender_id = :userId OR f.receiver_id = :userId AND f.request_status = 1"
    )
    List<Long> getAllFriends(@Param("userId") Long userId);
    @Query(
            nativeQuery = true,
            value = "SELECT f.* FROM friendships f WHERE f.sender_id = :userId AND f.request_status = 0"
    )
    List<Long> getAllSentRequests(@Param("userId") Long userId);
    @Query(
            nativeQuery = true,
            value = "SELECT f.* FROM friendships f WHERE f.receiver_id = :userId AND f.request_status = 0"
    )
    List<Long> getAllReceivedRequests(@Param("userId") Long userId);
}

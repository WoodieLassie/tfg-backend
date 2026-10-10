package es.judith.controller;

import es.judith.domain.Friend;
import es.judith.dto.friend.FriendDTO;
import es.judith.dto.friend.FriendInputDTO;
import es.judith.dto.user.UserProfileDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.Map;

public interface FriendController extends Serializable {
    ResponseEntity<Page<Map.Entry<Long, UserProfileDTO>>> findAllFriends(Integer page, Integer size, Pageable pageable);
    ResponseEntity<Page<Map.Entry<Long, UserProfileDTO>>> findAllSentRequests(Integer page, Integer size, Pageable pageable);
    ResponseEntity<Page<Map.Entry<Long, UserProfileDTO>>> findAllReceivedRequests(Integer page, Integer size, Pageable pageable);
    ResponseEntity<Friend> sendRequest(FriendInputDTO friendInputDTO);
    ResponseEntity<FriendDTO> acceptRequest(Long requestId);
    ResponseEntity<FriendDTO> delete(Long requestId);
}

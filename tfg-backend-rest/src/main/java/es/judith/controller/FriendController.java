package es.judith.controller;

import es.judith.domain.Friend;
import es.judith.dto.FriendDTO;
import es.judith.dto.FriendInputDTO;
import es.judith.dto.UserDTO;
import es.judith.dto.UserProfileDTO;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface FriendController extends Serializable {
    ResponseEntity<Map<Long, UserProfileDTO>> findAllFriends(Long userId);
    ResponseEntity<Map<Long, UserProfileDTO>> findAllSentRequests(Long userId);
    ResponseEntity<Map<Long, UserProfileDTO>> findAllReceivedRequests(Long userId);
    ResponseEntity<Friend> sendRequest(FriendInputDTO friendInputDTO);
    ResponseEntity<FriendDTO> acceptRequest(Long requestId);
    ResponseEntity<FriendDTO> delete(Long requestId);
}

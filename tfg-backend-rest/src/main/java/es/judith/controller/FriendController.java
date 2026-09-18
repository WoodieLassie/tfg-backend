package es.judith.controller;

import es.judith.domain.Friend;
import es.judith.dto.FriendDTO;
import es.judith.dto.FriendInputDTO;
import es.judith.dto.UserDTO;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface FriendController extends Serializable {
    ResponseEntity<Map<Long, UserDTO>> findAllFriends(Long userId);
    ResponseEntity<Map<Long, UserDTO>> findAllSentRequests(Long userId);
    ResponseEntity<Map<Long, UserDTO>> findAllReceivedRequests(Long userId);
    ResponseEntity<Friend> sendRequest(FriendInputDTO friendInputDTO);
    ResponseEntity<FriendDTO> acceptRequest(Long requestId);
    ResponseEntity<FriendDTO> delete(Long requestId);
}

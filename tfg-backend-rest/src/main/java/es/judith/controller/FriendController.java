package es.judith.controller;

import es.judith.domain.Friend;
import es.judith.dto.FriendDTO;
import es.judith.dto.FriendInputDTO;
import es.judith.dto.UserDTO;
import es.judith.rest.BaseController;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FriendController extends BaseController {
    ResponseEntity<List<UserDTO>> findAllFriends(Long userId);
    ResponseEntity<List<UserDTO>> findAllSentRequests(Long userId);
    ResponseEntity<List<UserDTO>> findAllReceivedRequests(Long userId);
    ResponseEntity<Friend> sendRequest(FriendInputDTO friendInputDTO);
    ResponseEntity<FriendDTO> acceptRequest(Long requestId);
    ResponseEntity<FriendDTO> delete(Long requestId);
}

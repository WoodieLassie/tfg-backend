package es.judith.bo;

import es.judith.domain.Friend;
import es.judith.dto.UserDTO;

import java.util.List;

public interface FriendBO extends GenericCRUDService<Friend, Long>{
    List<UserDTO> getAllFriends(Long userId);
    List<UserDTO> getAllSentRequests(Long userId);
    List<UserDTO> getAllReceivedRequests(Long userId);
}

package es.judith.bo;

import es.judith.domain.Friend;
import es.judith.dto.UserDTO;

import java.util.List;
import java.util.Map;

public interface FriendBO extends GenericCRUDService<Friend, Long>{
    Map<Long, UserDTO> getAllFriends(Long userId);
    Map<Long, UserDTO> getAllSentRequests(Long userId);
    Map<Long, UserDTO> getAllReceivedRequests(Long userId);
    boolean checkIfRelated(Long userId, Long friendId);
    boolean checkIfFriend(Long userId, Long friendId);
    boolean checkIfRequestReceiver(Long userId, Long friendId);
}

package es.judith.bo;

import es.judith.domain.Friend;
import es.judith.dto.UserDTO;
import es.judith.dto.UserProfileDTO;

import java.util.Map;

public interface FriendBO extends GenericBO<Friend, Long> {
    Map<Long, UserProfileDTO> getAllFriends(Long userId);
    Map<Long, UserProfileDTO> getAllSentRequests(Long userId);
    Map<Long, UserProfileDTO> getAllReceivedRequests(Long userId);
    boolean checkIfRelated(Long userId, Long friendId);
    boolean checkIfFriend(Long userId, Long friendId);
    boolean checkIfRequestReceiver(Long userId, Long friendId);
    Map<Long, UserProfileDTO> convertToDTO(Map<Long, Long> friendRequests);
}

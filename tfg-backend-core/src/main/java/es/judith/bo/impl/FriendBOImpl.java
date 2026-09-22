package es.judith.bo.impl;

import es.judith.bo.FriendBO;
import es.judith.dao.FriendRepository;
import es.judith.dao.UserRepository;
import es.judith.domain.Friend;
import es.judith.domain.User;
import es.judith.dto.UserDTO;
import es.judith.dto.UserProfileDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class FriendBOImpl extends GenericBOImpl<Friend, Long, FriendRepository> implements FriendBO {

    private final transient UserRepository userRepository;
    public FriendBOImpl(FriendRepository repository, UserRepository userRepository) {
        super(repository);
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Map<Long, UserProfileDTO> getAllFriends(Long userId) {
        List<Friend> friends = this.repository.getAllFriends(userId);
        Map<Long, Long> friendIds = new HashMap<>();
        for (Friend friend : friends) {
            if (Objects.equals(friend.getUserReceiver().getId(), userId)) {
                friendIds.put(friend.getId(), friend.getUserSender().getId());
            }
            else {
                friendIds.put(friend.getId(), friend.getUserReceiver().getId());
            }
        }
        return convertToDTO(friendIds);
    }

    @Transactional(readOnly = true)
    @Override
    public Map<Long, UserProfileDTO> getAllSentRequests(Long userId) {
        List<Friend> sentRequests = this.repository.getAllSentRequests(userId);
        Map<Long, Long> requestSentToIds = new HashMap<>();
        for (Friend sentRequest : sentRequests) {
            requestSentToIds.put(sentRequest.getId(), sentRequest.getUserReceiver().getId());
        }
        return convertToDTO(requestSentToIds);
    }

    @Transactional(readOnly = true)
    @Override
    public Map<Long, UserProfileDTO> getAllReceivedRequests(Long userId) {
        List<Friend> receivedRequests = this.repository.getAllReceivedRequests(userId);
        Map<Long, Long> requestedByIds = new HashMap<>();
        for (Friend receivedRequest : receivedRequests) {
            requestedByIds.put(receivedRequest.getId(), receivedRequest.getUserSender().getId());
        }
        return convertToDTO(requestedByIds);
    }
    @Transactional(readOnly = true)
    @Override
    public boolean checkIfRelated(Long userId, Long friendId) {
        Friend friendship = repository.getBySenderAndReceiverId(userId, friendId);
        if (friendship == null) {
            friendship = repository.getBySenderAndReceiverId(friendId, userId);
            return friendship != null;
        }
        return true;
    }
    @Transactional(readOnly = true)
    @Override
    public boolean checkIfFriend(Long userId, Long friendId) {
        Friend friendship = repository.getBySenderAndReceiverId(userId, friendId);
        if (friendship == null) {
            friendship = repository.getBySenderAndReceiverId(friendId, userId);
            if (friendship == null) {
                return false;
            }
        }
        return friendship.isRequestStatus();
    }
    @Transactional(readOnly = true)
    @Override
    public boolean checkIfRequestReceiver(Long userId, Long friendId) {
        Friend friendship = repository.getBySenderAndReceiverId(friendId, userId);
        if (friendship == null) {
            return false;
        }
        return Objects.equals(friendship.getUserReceiver().getId(), userId);
    }

    @Override
    public Map<Long, UserProfileDTO> convertToDTO(Map<Long, Long> friendRequests) {
        Map<Long, UserProfileDTO> friendRequestsWithUserInfo = new HashMap<>();
        for(Map.Entry<Long, Long> friendRequest : friendRequests.entrySet()){
            UserProfileDTO userDTO = new UserProfileDTO();
            Optional<User> optionalUser = userRepository.findById(friendRequest.getValue());
            optionalUser.ifPresent(userDTO::loadFromDomain);
            friendRequestsWithUserInfo.put(friendRequest.getKey(), userDTO);
        }
        return friendRequestsWithUserInfo;
    }
}

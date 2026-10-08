package es.judith.bo.impl;

import es.judith.bo.FriendBO;
import es.judith.bo.UserBO;
import es.judith.dao.FriendRepository;
import es.judith.dao.UserRepository;
import es.judith.domain.Friend;
import es.judith.domain.user.User;
import es.judith.dto.user.UserProfileDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serial;
import java.util.*;

@Service
@Transactional
public class FriendBOImpl extends GenericBOImpl<Friend, Long, FriendRepository> implements FriendBO {

    private final transient UserBO userBO;
    @Serial
    private static final long serialVersionUID = 3262767552083375561L;
    private static final Logger LOG = LoggerFactory.getLogger(FriendBOImpl.class);

    public FriendBOImpl(FriendRepository repository, UserBO userBO) {
        super(repository);
        this.userBO = userBO;
    }

    @Transactional(readOnly = true)
    @Override
    public Map<Long, UserProfileDTO> getAllFriends(Long userId) {
        LOG.debug("FriendBOImpl: getAllFriends");
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
        LOG.debug("FriendBOImpl: getAllSentRequests");
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
        LOG.debug("FriendBOImpl: getAllReceivedRequests");
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
        LOG.debug("FriendBOImpl: checkIfRelated");
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
        LOG.debug("FriendBOImpl: checkIfFriend");
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
        LOG.debug("FriendBOImpl: checkIfRequestReceiver");
        Friend friendship = repository.getBySenderAndReceiverId(friendId, userId);
        if (friendship == null) {
            return false;
        }
        return Objects.equals(friendship.getUserReceiver().getId(), userId);
    }

    @Override
    public Map<Long, UserProfileDTO> convertToDTO(Map<Long, Long> friendRequests) {
        LOG.debug("FriendBOImpl: convertToDTO");
        Map<Long, UserProfileDTO> friendRequestsWithUserInfo = new HashMap<>();
        for(Map.Entry<Long, Long> friendRequest : friendRequests.entrySet()){
            UserProfileDTO userDTO = new UserProfileDTO();
            User user = userBO.findOne(friendRequest.getValue());
            userDTO.loadFromDomain(user);
            friendRequestsWithUserInfo.put(friendRequest.getKey(), userDTO);
        }
        return friendRequestsWithUserInfo;
    }
}

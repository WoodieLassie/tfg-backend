package es.judith.bo.impl;

import es.judith.bo.FriendBO;
import es.judith.dao.FriendRepository;
import es.judith.dao.UserRepository;
import es.judith.domain.Friend;
import es.judith.domain.User;
import es.judith.dto.UserDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
//TODO: función boolean isFriend(userId, friendId) para instancias en las que la web necesite verificar el estado de amistad de un usuario, para mensajería, visualización de perfil, y recomendaciones
public class FriendBOImpl extends ElvisGenericCRUDServiceImpl<Friend, Long, FriendRepository> implements FriendBO {

    private final transient UserRepository userRepository;
    public FriendBOImpl(FriendRepository repository, UserRepository userRepository) {
        super(repository);
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDTO> getAllFriends(Long userId) {
        List<Long> friendIds = this.repository.getAllFriends(userId);
        return convertToDTO(friendIds);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDTO> getAllSentRequests(Long userId) {
        List<Long> requestSentToIds = this.repository.getAllSentRequests(userId);
        return convertToDTO(requestSentToIds);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDTO> getAllReceivedRequests(Long userId) {
        List<Long> requestedByIds = this.repository.getAllReceivedRequests(userId);
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
        if (!friendship.isRequestStatus()) {
            friendship = repository.getBySenderAndReceiverId(friendId, userId);
            return friendship.isRequestStatus();
        }
        return true;
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

    public List<UserDTO> convertToDTO(List<Long> userIds) {
        List<UserDTO> userDTOs = new LinkedList<>();
        for(Long id: userIds){
            UserDTO userDTO = new UserDTO();
            Optional<User> optionalUser = userRepository.findById(id);
            optionalUser.ifPresent(userDTO::loadFromDomain);
            userDTOs.add(userDTO);
        }
        return userDTOs;
    }
}

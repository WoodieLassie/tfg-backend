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
import java.util.Optional;

@Service
@Transactional
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

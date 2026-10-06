package es.judith.bo.impl;

import es.judith.bo.ChatRoomBO;
import es.judith.dao.ChatRoomRepository;
import es.judith.dao.UserRepository;
import es.judith.domain.chat.ChatRoom;
import es.judith.domain.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ChatRoomBOImpl extends GenericBOImpl<ChatRoom, Long, ChatRoomRepository> implements ChatRoomBO {

    private final transient UserRepository userRepository;

    public ChatRoomBOImpl(ChatRoomRepository repository, UserRepository userRepository) {
        super(repository);
        this.userRepository = userRepository;
    }

    @Override
    public Optional<String> getChatRoomReference(Long senderId, Long receiverId, boolean createNewRoomIfNotExists) {
        return repository.findBySenderIdAndReceiverId(senderId, receiverId)
                .map(ChatRoom::getChatRoomReference)
                .or (() -> {
                    if (createNewRoomIfNotExists) {
                        Optional<User> userSender = userRepository.findById(senderId);
                        Optional<User> userReceiver = userRepository.findById(receiverId);
                        if (userSender.isPresent() && userReceiver.isPresent()) {
                            String chatReference = createChatRoomReference(userSender.get(), userReceiver.get());
                            return Optional.of(chatReference);
                        }
                    }
                    return Optional.empty();
                });
    }

    @Override
    public String createChatRoomReference(User userSender, User userReceiver) {
        String chatRoomReference = String.format("%s_%s", userSender.getId(), userReceiver.getId());
        ChatRoom senderReceiver = ChatRoom.builder()
                .chatRoomReference(chatRoomReference)
                .userSender(userSender)
                .userReceiver(userReceiver)
                .build();

        ChatRoom receiverSender = ChatRoom.builder()
                .chatRoomReference(chatRoomReference)
                .userSender(userReceiver)
                .userReceiver(userSender)
                .build();
        repository.save(senderReceiver);
        repository.save(receiverSender);
        return chatRoomReference;
    }

    @Override
    public Optional<ChatRoom> findByChatRoomReference(String chatRoomReference) {
        return repository.findByChatRoomReference(chatRoomReference);
    }
}

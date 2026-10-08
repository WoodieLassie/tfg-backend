package es.judith.bo.impl;

import es.judith.bo.ChatRoomBO;
import es.judith.bo.UserBO;
import es.judith.dao.ChatRoomRepository;
import es.judith.dao.UserRepository;
import es.judith.domain.chat.ChatRoom;
import es.judith.domain.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serial;
import java.util.Optional;

@Service
@Transactional
public class ChatRoomBOImpl extends GenericBOImpl<ChatRoom, Long, ChatRoomRepository> implements ChatRoomBO {

    private final transient UserBO userBO;
    @Serial
    private static final long serialVersionUID = -8475951517591541787L;
    private static final Logger LOG = LoggerFactory.getLogger(ChatRoomBOImpl.class);

    public ChatRoomBOImpl(ChatRoomRepository repository, UserBO userBO) {
        super(repository);
        this.userBO = userBO;
    }

    @Override
    public Optional<String> getChatRoomReference(Long senderId, Long receiverId, boolean createNewRoomIfNotExists) {
        LOG.debug("ChatRoomBOImpl: getChatRoomReference");
        return repository.findBySenderIdAndReceiverId(senderId, receiverId)
                .map(ChatRoom::getChatRoomReference)
                .or (() -> {
                    if (createNewRoomIfNotExists) {
                        User userSender = userBO.findOne(senderId);
                        User userReceiver = userBO.findOne(receiverId);
                        if (userSender != null && userReceiver != null) {
                            String chatReference = createChatRoomReference(userSender, userReceiver);
                            return Optional.of(chatReference);
                        }
                    }
                    return Optional.empty();
                });
    }

    @Override
    public String createChatRoomReference(User userSender, User userReceiver) {
        LOG.debug("ChatRoomBOImpl: createChatRoomReference");
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
        LOG.debug("ChatRoomBOImpl: findByChatRoomReference");
        return repository.findByChatRoomReference(chatRoomReference);
    }
}

package es.judith.bo.impl;

import es.judith.bo.ChatMessageBO;
import es.judith.bo.ChatRoomBO;
import es.judith.dao.ChatMessageRepository;
import es.judith.domain.chat.ChatMessage;
import es.judith.domain.chat.ChatRoom;
import es.judith.dto.chat.ChatMessageDTO;
import es.judith.dto.user.UserProfileDTO;
import es.judith.exceptions.BadInputException;
import es.judith.exceptions.NotExistingIdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serial;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ChatMessageBOImpl extends GenericBOImpl<ChatMessage, Long, ChatMessageRepository> implements ChatMessageBO {

    private final transient ChatRoomBO chatRoomBO;
    @Serial
    private static final long serialVersionUID = 626399586736149127L;
    private static final Logger LOG = LoggerFactory.getLogger(ChatMessageBOImpl.class);

    public ChatMessageBOImpl(ChatMessageRepository repository, ChatRoomBO chatRoomBO) {
        super(repository);
        this.chatRoomBO = chatRoomBO;
    }

    @Override
    public ChatMessage saveMessage(ChatMessage chatMessage)  {
        LOG.debug("ChatMessageBOImpl: saveMessage");
        String chatRoomReference = chatRoomBO.getChatRoomReference(
                chatMessage.getSenderUser().getId(),
                chatMessage.getReceiverUser().getId(),
                true).orElseThrow(BadInputException::new);
        ChatRoom chatroom = chatRoomBO.findByChatRoomReference(chatRoomReference).orElseThrow(NotExistingIdException::new);
        chatMessage.setChatRoom(chatroom);
        chatMessage.setTimestamp(new Date());
        repository.save(chatMessage);
        return chatMessage;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessage> findChatMessages(Long senderId, Long receiverId) {
        LOG.debug("ChatMessageBOImpl: findChatMessages");
        String chatRoomReference = chatRoomBO.getChatRoomReference(senderId, receiverId, false).orElseThrow(NotExistingIdException::new);
        ChatRoom chatRoom = chatRoomBO.findByChatRoomReference(chatRoomReference).orElseThrow(NotExistingIdException::new);
        return repository.findAllByChatRoomId(chatRoom.getId());
    }

    @Override
    public ChatMessageDTO convertToDTO(ChatMessage chatMessage) {
        ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
        UserProfileDTO userSenderProfileDTO = new UserProfileDTO();
        UserProfileDTO userReceiverProfileDTO = new UserProfileDTO();
        userSenderProfileDTO.loadFromDomain(chatMessage.getSenderUser());
        userReceiverProfileDTO.loadFromDomain(chatMessage.getReceiverUser());
        chatMessageDTO.loadFromDomain(chatMessage);
        chatMessageDTO.setUserSender(userSenderProfileDTO);
        chatMessageDTO.setUserReceiver(userReceiverProfileDTO);
        return chatMessageDTO;
    }
}

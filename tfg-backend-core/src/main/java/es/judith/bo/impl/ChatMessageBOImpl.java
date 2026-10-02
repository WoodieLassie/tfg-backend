package es.judith.bo.impl;

import es.judith.bo.ChatMessageBO;
import es.judith.bo.ChatRoomBO;
import es.judith.dao.ChatMessageRepository;
import es.judith.domain.ChatMessage;
import es.judith.domain.ChatRoom;
import es.judith.exceptions.BadInputException;
import es.judith.exceptions.NotExistingIdException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ChatMessageBOImpl extends GenericBOImpl<ChatMessage, Long, ChatMessageRepository> implements ChatMessageBO {

    private final transient ChatRoomBO chatRoomBO;

    public ChatMessageBOImpl(ChatMessageRepository repository, ChatRoomBO chatRoomBO) {
        super(repository);
        this.chatRoomBO = chatRoomBO;
    }

    @Override
    public ChatMessage saveMessage(ChatMessage chatMessage)  { //ChatMessageInputDTO chatMessageDTO
        String chatRoomReference = chatRoomBO.getChatRoomReference(
                //chatMessageDTO.getSenderId, chatMessageDTO.getReceiverId
                chatMessage.getSenderUser().getId(),
                chatMessage.getReceiverUser().getId(),
                true).orElseThrow(BadInputException::new);
        ChatRoom chatroom = chatRoomBO.findByChatRoomReference(chatRoomReference).orElseThrow(NotExistingIdException::new);
        //ChatMessage chatMessage = chatMessageDTO.obtainDomainObject
        chatMessage.setChatRoom(chatroom);
        repository.save(chatMessage);
        return chatMessage;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessage> findChatMessages(Long senderId, Long receiverId) {
        String chatRoomReference = chatRoomBO.getChatRoomReference(senderId, receiverId, false).orElseThrow(NotExistingIdException::new);
        ChatRoom chatRoom = chatRoomBO.findByChatRoomReference(chatRoomReference).orElseThrow(NotExistingIdException::new);
        return repository.findAllByChatRoomId(chatRoom.getId());
    }
}

package es.judith.dto.chat;

import es.judith.domain.chat.ChatMessage;
import es.judith.domain.chat.ChatRoom;
import es.judith.domain.user.User;
import es.judith.dto.GenericDTO;
import es.judith.dto.user.UserProfileDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

@Schema(name = "ChatMessageDTO", description = "Data transfer object. Chat message")
@EqualsAndHashCode(callSuper = true)
@Data
public class ChatMessageDTO extends GenericDTO<ChatMessage> {
    @Serial
    private static final long serialVersionUID = 1840052432968449570L;

    @NotNull private ChatRoom chatRoom;
    @NotNull private UserProfileDTO userSender;
    @NotNull private UserProfileDTO userReceiver;
    @NotNull private String content;
    @NotNull private Date timestamp;
}

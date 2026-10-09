package es.judith.dto.chat;

import es.judith.domain.chat.ChatRoom;
import es.judith.dto.GenericDTO;
import es.judith.dto.user.UserProfileDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Schema(name = "ChatRoomDTO", description = "Data transfer object. Chat room")
@EqualsAndHashCode(callSuper = true)
@Data
public class ChatRoomDTO extends GenericDTO<ChatRoom> {
    @Serial
    private static final long serialVersionUID = -4617271513903570885L;

    @NotNull private String chatRoomReference;
}

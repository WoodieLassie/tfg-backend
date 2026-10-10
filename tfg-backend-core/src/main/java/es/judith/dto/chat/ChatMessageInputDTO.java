package es.judith.dto.chat;


import com.fasterxml.jackson.annotation.JsonIgnore;
import es.judith.domain.chat.ChatMessage;
import es.judith.dto.GenericDTO;
import es.judith.dto.user.UserProfileDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Stream;

@Schema(name = "ChatMessageInputDTO", description = "Data transfer object for input. Chat message")
@EqualsAndHashCode(callSuper = true)
@Data
public class ChatMessageInputDTO extends GenericDTO<ChatMessage> {
    @Serial
    private static final long serialVersionUID = -2459526079015512131L;

    @NotNull @JsonIgnore private Long userSenderId;
    @NotNull private Long userReceiverId;
    @NotNull private String content;
    @NotNull @JsonIgnore private Date timestamp;

    public boolean allFieldsArePresent() {return Stream.of(this.userReceiverId, this.content).allMatch(Objects::nonNull);}
}

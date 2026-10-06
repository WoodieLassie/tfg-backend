package es.judith.domain.chat;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@XmlRootElement
@EqualsAndHashCode
@Data
@Builder
public class ChatNotification {
    @NotNull
    private String chatRoomReference;
    @NotNull
    private String userSenderName;
    @NotNull
    private String userReceiverName;
    @NotNull
    private String content;
}

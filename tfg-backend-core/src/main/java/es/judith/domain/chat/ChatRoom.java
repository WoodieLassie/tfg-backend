package es.judith.domain.chat;

import es.judith.domain.GenericEntity;
import es.judith.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.io.Serial;

@Entity
@XmlRootElement
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chat_rooms")
public class ChatRoom extends GenericEntity {
    @Serial private static final long serialVersionUID = 7651127467545150684L;

    @Column(name = "chat_room_reference", nullable = false, length = 100)
    @NotNull
    private String chatRoomReference;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User userSender;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User userReceiver;
}

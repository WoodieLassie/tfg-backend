package es.judith.domain;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

@Entity
@XmlRootElement
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chat_rooms")
public class ChatRoom extends GenericEntity {
    private String chatRoomReference;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User userSender;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User userReceiver;
}

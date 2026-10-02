package es.judith.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Entity
@XmlRootElement
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "chat_messages")
public class ChatMessage extends GenericEntity {
    @ManyToOne
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User senderUser;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiverUser;
    @Column(name = "content", nullable = false)
    @NotNull
    private String content;
    @Column(name = "timestamp", nullable = false)
    @NotNull
    private Date timestamp;
}

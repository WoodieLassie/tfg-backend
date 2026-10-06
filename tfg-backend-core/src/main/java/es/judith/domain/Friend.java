package es.judith.domain;

import es.judith.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Entity
@XmlRootElement
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "friendships")
public class Friend extends GenericEntity {
    @Serial private static final long serialVersionUID = 3565593854101402250L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User userSender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User userReceiver;

    @Column(name = "request_status", nullable = false)
    @NotNull
    private boolean requestStatus;

}
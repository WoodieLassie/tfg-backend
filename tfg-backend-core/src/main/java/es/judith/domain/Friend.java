package es.judith.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.NaturalId;

@Entity
@XmlRootElement
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "friendships")
public class Friend extends GenericEntity {

    @NaturalId
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User userSender;

    @NaturalId
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User userReceiver;

    @Column(name = "request_status", nullable = false)
    @NotNull
    private boolean requestStatus;

}
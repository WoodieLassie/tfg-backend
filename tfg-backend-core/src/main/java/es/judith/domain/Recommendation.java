package es.judith.domain;

import es.judith.domain.user.User;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@XmlRootElement
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "recommendations")
public class Recommendation extends GenericEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User userSender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User userReceiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "show_id")
    private Show show;
}

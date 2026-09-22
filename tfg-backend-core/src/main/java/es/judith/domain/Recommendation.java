package es.judith.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.NaturalId;

@Entity
@XmlRootElement
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "recommendations")
public class Recommendation extends GenericEntity {

    @NaturalId
    @OneToOne
    @JoinColumn(name = "sender_id")
    private User userSender;

    @NaturalId
    @OneToOne
    @JoinColumn(name = "receiver_id")
    private User userReceiver;

    @NaturalId
    @OneToOne
    @JoinColumn(name = "show_id")
    private Show show;
}

package es.judith.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import es.judith.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Entity
@XmlRootElement
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "comments")
public class Comment extends GenericEntity {
  @Serial
  private static final long serialVersionUID = 8182917368975655915L;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "text", nullable = false)
  @NotNull
  @Size(max = 255)
  private String text;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "show_id")
  @JsonBackReference
  private Show show;
}

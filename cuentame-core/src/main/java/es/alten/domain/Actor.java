package es.alten.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

@Entity
@XmlRootElement
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "actors")
public class Actor extends Audit {
  @Serial private static final long serialVersionUID = 1887096612684365214L;

  @Column(name = "name", nullable = false)
  @NotNull
  private String name;

  @Column(name = "birth_date", nullable = false)
  @NotNull
  private Date birthDate;

  @Column(name = "nationality", nullable = false)
  @NotNull
  private String nationality;

  @Column(name = "gender", nullable = false)
  @NotNull
  private String gender;

  @Column(name = "birth_location", nullable = false)
  private String birthLocation;

  @ManyToOne
  @JoinColumn(name = "character_id", nullable = false)
  private Character character;
}

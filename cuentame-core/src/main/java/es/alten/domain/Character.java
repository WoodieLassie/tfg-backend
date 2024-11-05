package es.alten.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

@Entity
@XmlRootElement
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "characters")
public class Character extends Audit {
  @Serial private static final long serialVersionUID = -4530630569431995861L;

  @Column(name = "name", nullable = false)
  @NotNull
  private String name;

  @Column(name = "description", nullable = false)
  @NotNull
  private String description;

  @Column(name = "gender", nullable = false)
  @NotNull
  private String gender;

  @Column(name = "nationality", nullable = false)
  @NotNull
  private String nationality;

  @Column(name = "age", nullable = false)
  @NotNull
  private Integer age;

  @OneToMany
  private List<Actor> actors;
}

package es.alten.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

  @Column(name = "name", nullable = false, length = 100)
  @NotNull
  @Size(max = 100)
  private String name;

  @Column(name = "description", nullable = false, length = 100)
  @NotNull
  @Size(max = 100)
  private String description;

  @Column(name = "gender", nullable = false, length = 100)
  @NotNull
  @Size(max = 100)
  private String gender;

  @Column(name = "nationality", nullable = false, length = 100)
  @NotNull
  @Size(max = 100)
  private String nationality;

  @Column(name = "age", nullable = false)
  @NotNull
  private Integer age;

  @OneToMany
  private List<Actor> actors;
}

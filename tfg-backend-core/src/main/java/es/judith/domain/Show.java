package es.judith.domain;

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
@Table(name = "shows")
public class Show extends Audit {
  @Serial
  private static final long serialVersionUID = 7575277469267296146L;

  @Column(name = "name", nullable = false)
  @NotNull
  private String name;

  @Column(name = "description", nullable = false, length = 100)
  @NotNull
  @Size(max = 100)
  private String description;
}

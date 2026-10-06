package es.judith.domain;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.Data;

@MappedSuperclass
@Data
public abstract class GenericEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}

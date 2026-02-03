package es.judith.domain;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Interface to mark every entity in the system.
 */
@MappedSuperclass
@Data
public abstract class ElvisEntity implements Serializable, Cloneable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}

package es.alten.dto;

import es.alten.domain.ElvisEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;

/**
 * DTO to transfer search filter information.
 *
 * @author joseluis.osorno
 */
@Data
public abstract class BaseFilterDTO<T extends ElvisEntity> implements Serializable {

  /** serialVersionUID for object serialization */
  private static final long serialVersionUID = -1199816929624124209L;

  /**
   * Get specification object to filter entities with filter object conditions.
   *
   * @return Specification object to filter entities with filter object conditions.
   */
  public abstract Specification<T> obtainFilterSpecification();

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this, false);
  }

  /** @noinspection all */
  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }
}

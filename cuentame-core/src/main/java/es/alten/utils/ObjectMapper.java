package es.alten.utils;

import lombok.Data;
import org.dozer.CustomFieldMapper;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.classmap.ClassMap;
import org.dozer.fieldmap.FieldMap;
import org.hibernate.Hibernate;
import org.springframework.data.mapping.MappingException;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Component to map Entity to DTO and vice versa. It also prevents Hibernate entities with
 * FetchType.LAZY to cause a LazyInitializationException; they simply stay null.
 *
 * @author delivery
 */
@Component
@Data
public class ObjectMapper implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 3863486665328798424L;

  /** Singleton instance of the class. */
  private static final ObjectMapper instance;

  /** Internal Dozer mapper. */
  private static Mapper mapper = new DozerBeanMapper();

  /* Class initialization. */
  static {
    instance = new ObjectMapper();
  }

  /** Constructor. */
  private ObjectMapper() {
    ((DozerBeanMapper) mapper).setCustomFieldMapper(new HibernateInitializedFieldMapper());
  }

  public static ObjectMapper getInstance() {
    return instance;
  }

  /**
   * Maps the source object into the destination object.
   *
   * @param source Object to be map
   * @param destination Object to receive the information of the source object
   * @throws MappingException if cannot map the source
   */
  public void map(final Object source, final Object destination) {
    try {
      mapper.map(source, destination);
    } catch (final org.dozer.MappingException e) {
      // Dozer MappingException is caught and a Spring Mapping Exception
      // is thrown
      throw new MappingException("Mapping exception", e);
    }
  }

  /**
   * Custom field mapper that prevents Dozer from throwing a LazyInitializationException if one
   * field from the source object has FetchType.LAZY.
   */
  private class HibernateInitializedFieldMapper implements CustomFieldMapper {
    @Override
    public boolean mapField(
        final Object source,
        final Object destination,
        final Object sourceFieldValue,
        final ClassMap classMap,
        final FieldMap fieldMapping) {
      // If field is initialized, Dozer will continue mapping
      return !Hibernate.isInitialized(sourceFieldValue);
    }
  }
}

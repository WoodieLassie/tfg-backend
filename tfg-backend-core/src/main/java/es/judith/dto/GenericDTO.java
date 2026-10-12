package es.judith.dto;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mapping.MappingException;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.judith.domain.GenericEntity;
import es.judith.utils.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "GenericDTO", description = "Generic data transfer object")
@Data
public abstract class GenericDTO<T extends GenericEntity> implements Serializable {

  @Serial
  private static final long serialVersionUID = 3212686467056680847L;
  private static final Logger LOG = LoggerFactory.getLogger(GenericDTO.class);
  @JsonIgnore
  private final ObjectMapper mapper;
  @JsonIgnore
  private final Class<T> entityType;
  @Schema
  private Long id;
  @SuppressWarnings("unchecked")
  protected GenericDTO() {
    // Gets form type used by child controller
    final ParameterizedType genericSuperclass =
        (ParameterizedType) getClass().getGenericSuperclass();

    entityType = (Class<T>) genericSuperclass
        .getActualTypeArguments()[genericSuperclass.getActualTypeArguments().length - 1];

    // Initializes the object mapper
    mapper = ObjectMapper.getInstance();
  }

  public void loadFromDomain(final T entity) {
    try {
      mapper.map(entity, this);
    } catch (final MappingException e) {
      LOG.error(String.format("BaseDTO : loadFromDomain, Mapping exception with type %s",
          entityType.getName()));
    }
  }

  public T obtainDomainObject() {
    T entity = null;

    try {
      entity = entityType.getDeclaredConstructor().newInstance();
      mapper.map(this, entity);
    } catch (final MappingException e) {
      LOG.error(String.format("BaseDTO : obtainDomainObject, Mapping exception with type %s",
          entityType.getName()));
    } catch (final InstantiationException | IllegalAccessException e) {
      LOG.error(
          String.format("BaseDTO : obtainDomainObject, Unable to instantiate an object of type %s",
              entityType.getName()));
    } catch (final Exception e) {
      LOG.error(String.format(
          "Exception in BaseDTO : obtainDomainObject, Unable to instantiate an object of type %s",
          entityType.getName()));
    }
    return entity;
  }
}

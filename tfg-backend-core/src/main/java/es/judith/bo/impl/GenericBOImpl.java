package es.judith.bo.impl;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import es.judith.bo.GenericBO;
import es.judith.dao.GenericRepository;
import es.judith.domain.GenericEntity;

public class GenericBOImpl<T extends GenericEntity, I extends Serializable, R extends GenericRepository<T, I>>
    implements GenericBO<T, I> {

  @Serial
  private static final long serialVersionUID = -4005659813031548678L;
  private static final Logger LOG = LoggerFactory.getLogger(GenericBOImpl.class);

  /**
   * Repository injected to this service.
   *
   * @noinspection SpringJavaAutowiredMembersInspection
   */
  protected final transient R repository;

  public GenericBOImpl(R repository) {
    super();
    this.repository = repository;
  }

  /**
   * Saves a given entity. Use the returned instance for further operations as the save operation
   * might have changed the entity instance completely.
   *
   * @param entity the entity
   * @return the saved entity
   */
  @Override
  public <S extends T> S save(final S entity) {
    LOG.debug("GenericBOImpl: save(S entity)");
    return repository.save(entity);
  }

  /**
   * Saves all given entities.
   *
   * @param entities the entities
   * @return the saved entities
   * @throws IllegalArgumentException in case the given entity is (@literal null}.
   */
  @Override
  public <S extends T> List<S> save(final List<S> entities) {
    LOG.debug("GenericBOImpl: save(List<S> entities)");
    return Collections.emptyList();
  }

  /**
   * Retrieves an entity by its id.
   *
   * @param id must not be {@literal null}.
   * @return the entity with the given id or {@literal null} if none found
   * @throws IllegalArgumentException if {@code id} is {@literal null}
   */
  @Override
  public T findOne(final I id) {
    LOG.debug("GenericBOImpl: findOne(I id)");
    return repository.findById(id).orElse(null);
  }

  /**
   * Returns whether an entity with the given id exists.
   *
   * @param id must not be {@literal null}.
   * @return true if an entity with the given id exists, {@literal false} otherwise
   * @throws IllegalArgumentException if {@code id} is {@literal null}
   */
  @Override
  public boolean exists(final I id) {
    LOG.debug("GenericBOImpl: exists(I id)");
    return repository.existsById(id);
  }

  /**
   * Returns all instances of the type.
   *
   * @return all entities
   */
  @Override
  public List<T> findAll() {
    LOG.debug("GenericBOImpl: findAll()");
    return repository.findAll();
  }

  /**
   * Find all instances of the type paginated.
   *
   * @param pageable Page requested.
   * @return Entities of the requested page.
   */
  @Override
  public Page<T> findAll(final Pageable pageable) {
    LOG.debug("GenericBOImpl: findAll(Pageable pageable)");
    return repository.findAll(pageable);
  }

  /**
   * Returns all instances of the type with the given IDs.
   *
   * @param ids list of ids
   * @return all entities with the ids
   */
  @Override
  public List<T> findAll(final List<I> ids) {
    LOG.debug("GenericBOImpl: findAll(List<I> ids)");
    return Collections.emptyList();
  }

  /**
   * Returns the number of entities available.
   *
   * @return the number of entities
   */
  @Override
  public long count() {
    LOG.debug("GenericBOImpl: count()");
    return 0;
  }

  /**
   * Deletes the entity with the given id.
   *
   * @param id must not be {@literal null}.
   * @throws IllegalArgumentException in case the given {@code id} is {@literal null}
   */
  @Override
  public void delete(final I id) {
    LOG.debug("GenericBOImpl: delete(I id)");
    repository.deleteById(id);
  }

  /**
   * Deletes a given entity.
   *
   * @param entity the entity
   * @throws IllegalArgumentException in case the given entity is (@literal null}.
   */
  @Override
  public void delete(final T entity) {
    LOG.debug("GenericBOImpl: delete(T entity)");
    repository.delete(entity);
  }

  /**
   * Deletes the given entities.
   *
   * @param entities the list of entities
   * @throws IllegalArgumentException in case the given {@link List} is (@literal null}.
   */
  @Override
  public void delete(final List<? extends T> entities) {
    LOG.debug("GenericBOImpl: List<T> entities");
    repository.deleteAll(entities);
  }

  /** Deletes all entities managed by the repository. */
  @Override
  public void deleteAll() {
    LOG.debug("GenericBOImpl: deleteAll()");
    repository.deleteAll();
  }

  @Override
  public boolean checkForIllegalStrings(String string, Integer maxLength) {
    Pattern regex = Pattern.compile("[^A-Za-z0-9 ]");
    Matcher matcher = regex.matcher(string);
    if (matcher.find()) {
      return true;
    }
    return string.length() > maxLength;
  }
}

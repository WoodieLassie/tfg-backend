package es.judith.bo.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import es.judith.bo.GenericCRUDService;
import es.judith.dao.ElvisBaseRepository;
import es.judith.domain.ElvisEntity;
import es.judith.dto.BaseFilterDTO;

public class ElvisGenericCRUDServiceImpl<T extends ElvisEntity, I extends Serializable, Q extends EntityPathBase<T>, F extends BaseFilterDTO<T>, R extends ElvisBaseRepository<T, I, Q>>
    implements GenericCRUDService<T, I, Q, F> {

  private static final long serialVersionUID = -4005659813031548678L;

  /**
   * Repository injected to this service.
   *
   * @noinspection SpringJavaAutowiredMembersInspection
   */
  protected final transient R repository;

  public ElvisGenericCRUDServiceImpl(R repository) {
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
    return repository.existsById(id);
  }

  /**
   * Returns all instances of the type.
   *
   * @return all entities
   */
  @Override
  public List<T> findAll() {
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
    return Collections.emptyList();
  }

  /**
   * Returns the number of entities available.
   *
   * @return the number of entities
   */
  @Override
  public long count() {
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
    repository.deleteAll(entities);
  }

  /** Deletes all entities managed by the repository. */
  @Override
  public void deleteAll() {
    repository.deleteAll();
  }

  /**
   * Find entities that fix with querydsl conditions established by predicate. Result is returned
   * listed.
   *
   * @param predicate filter
   * @return filter
   */
  @Override
  public List<T> findByQueryDsl(final Predicate predicate) {
    return repository.findAll(predicate);
  }

  /**
   * Find entities that fix with filter conditions established by filter object. Result is returned
   * paged.
   *
   * @param predicate Search conditions.
   * @param pageable Page information to return.
   * @return Page with entities that fix with filter conditions.
   */
  @Override
  public Page<T> findByQueryDsl(Predicate predicate, Pageable pageable) {
    return null;
  }

  /**
   * Find entities that fix with filter conditions established by filter object. Result is returned
   * paged.
   *
   * @param filter Search conditions.
   * @param pageable Page information to return.
   * @return Page with entities that fix with filter conditions.
   */
  @Override
  public Page<T> findByFilter(F filter, Pageable pageable) {
    return repository.findAll(filter.obtainFilterSpecification(), pageable);
  }

  /**
   * Find entities that fix with filter conditions established by filter object.
   *
   * @param filter Search conditions.
   * @return List with entities that fix with filter conditions.
   */
  @Override
  public List<T> findByFilter(F filter) {
    return repository.findAll(filter.obtainFilterSpecification());
  }
}

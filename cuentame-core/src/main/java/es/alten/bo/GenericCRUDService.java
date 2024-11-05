package es.alten.bo;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import es.alten.domain.ElvisEntity;
import es.alten.dto.BaseFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

/**
 * Generic interface that defines CRUD service operations. Generic service pattern.
 *
 * @param <T> Entity class to use in the pattern.
 * @param <I> Class identifier of the entity.
 * @param <F> Filter search DTO class to use in the pattern.
 * @param <Q> Q object
 * @author delivery
 * @noinspection unused
 */
public interface GenericCRUDService<
        T extends ElvisEntity,
        I extends Serializable,
        Q extends EntityPathBase<T>,
        F extends BaseFilterDTO<T>>
    extends Serializable {

  /**
   * Saves a given entity. Use the returned instance for further operations as the save operation
   * might have changed the entity instance completely.
   *
   * @param entity object
   * @return the saved entity
   */
  <S extends T> S save(S entity);

  /**
   * Saves all given entities.
   *
   * @param entities list of entities
   * @return the saved entities
   * @throws IllegalArgumentException in case the given entity is (@literal null}.
   * @noinspection unused
   */
  <S extends T> List<S> save(List<S> entities);

  /**
   * Retrieves an entity by its id.
   *
   * @param id must not be {@literal null}.
   * @return the entity with the given id or {@literal null} if none found
   * @throws IllegalArgumentException if {@code id} is {@literal null}
   */
  T findOne(I id);

  /**
   * Returns whether an entity with the given id exists.
   *
   * @param id must not be {@literal null}.
   * @return true if an entity with the given id exists, {@literal false} otherwise
   * @throws IllegalArgumentException if {@code id} is {@literal null}
   * @noinspection unused
   */
  boolean exists(I id);

  /**
   * Find all instances of the type paginated.
   *
   * @param pageable Page requested.
   * @return Entities of the requested page.
   */
  Page<T> findAll(Pageable pageable);

  /**
   * Returns all instances of the type with the given IDs.
   *
   * @param ids list of ids
   * @return all the entities with the ids
   * @noinspection unused
   */
  List<T> findAll(List<I> ids);

  /**
   * Returns all instances of the type.
   *
   * @return all entities
   */
  List<T> findAll();

  /**
   * Find entities that fix with filter conditions established by filter object. Result is returned
   * paged.
   *
   * @param predicate Search conditions.
   * @param pageable Page information to return.
   * @return Page with entities that fix with filter conditions.
   * @noinspection unused
   */
  Page<T> findByQueryDsl(Predicate predicate, Pageable pageable);

  /**
   * Find entities that fix with querydsl conditions established by predicate. Result is returned
   * listed.
   *
   * @param predicate filter
   * @return filter
   * @noinspection unused
   */
  List<T> findByQueryDsl(Predicate predicate);

  /**
   * Returns the number of entities available.
   *
   * @return the number of entities
   * @noinspection unused
   */
  long count();

  /**
   * Deletes the entity with the given id.
   *
   * @param id must not be {@literal null}.
   * @throws IllegalArgumentException in case the given {@code id} is {@literal null}
   * @noinspection unused
   */
  void delete(I id);

  /**
   * Deletes a given entity.
   *
   * @param entity the entity
   * @throws IllegalArgumentException in case the given entity is (@literal null}.
   */
  void delete(T entity);

  /**
   * Deletes the given entities.
   *
   * @param entities the list of entities
   * @throws IllegalArgumentException in case the given {@link List} is (@literal null}.
   * @noinspection unused
   */
  void delete(List<? extends T> entities);

  /** Deletes all entities managed by the repository. */
  void deleteAll();

  /**
   * Find entities that fix with filter conditions established by filter object. Result is returned
   * paged.
   *
   * @param filter Search conditions.
   * @param pageable Page information to return.
   * @return Page with entities that fix with filter conditions.
   */
  Page<T> findByFilter(F filter, Pageable pageable);

  /**
   * Find entities that fix with filter conditions established by filter object.
   *
   * @param filter Search conditions.
   * @return List with entities that fix with filter conditions.
   * @noinspection unused
   */
  List<T> findByFilter(F filter);
}

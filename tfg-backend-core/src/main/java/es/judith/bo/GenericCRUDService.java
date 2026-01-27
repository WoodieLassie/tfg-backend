package es.judith.bo;

import es.judith.domain.ElvisEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

/**
 * Generic interface that defines CRUD service operations. Generic service pattern.
 *
 * @param <T> Entity class to use in the pattern.
 * @param <I> Class identifier of the entity.
 * @author delivery
 * @noinspection unused
 */
public interface GenericCRUDService<
        T extends ElvisEntity,
        I extends Serializable>
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
}

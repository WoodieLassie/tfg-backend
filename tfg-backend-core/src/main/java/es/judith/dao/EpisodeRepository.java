package es.judith.dao;

import es.judith.domain.Character;
import es.judith.domain.Episode;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EpisodeRepository
    extends GenericRepository<Episode, Long>,
        JpaSpecificationExecutor<Episode> {

  @Query(
      "SELECT e FROM Episode e "
          + "LEFT JOIN FETCH e.season s "
          + "LEFT JOIN FETCH e.characters c WHERE e.id = :id")
  Optional<Episode> findById(@Param("id") Long id);

  @Query(
      "SELECT e FROM Episode e "
          + "LEFT JOIN FETCH e.season s "
          + "WHERE LOWER(e.title) LIKE LOWER(CONCAT('%', :title, '%')) ")
  List<Episode> findByTitle(
      @Param("title") String title);

  // Hace fetch de los actores de cada personaje en una query aparte para evitar
  // MultipleBagFetchException
  @Query("SELECT c FROM Character c " + "LEFT JOIN FETCH c.actors a " + "WHERE c.id IN :ids")
  List<Character> findByIdWithCharacters(@Param("ids") List<Long> ids);
}

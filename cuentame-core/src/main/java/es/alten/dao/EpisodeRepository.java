package es.alten.dao;

import es.alten.domain.Character;
import es.alten.domain.Episode;
import es.alten.domain.QEpisode;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EpisodeRepository
    extends ElvisBaseRepository<Episode, Long, QEpisode>,
        JpaSpecificationExecutor<Episode>,
        QuerydslPredicateExecutor<Episode>,
        QuerydslBinderCustomizer<QEpisode> {
  @Query("SELECT e FROM Episode e " + "JOIN FETCH e.season s " + "JOIN FETCH e.characters c")
  List<Episode> findAll();

  @Query(
      "SELECT e FROM Episode e "
          + "JOIN FETCH e.season s "
          + "JOIN FETCH e.characters c WHERE e.id = :id")
  Optional<Episode> findById(@Param("id") Long id);

  @Query(
      "SELECT e FROM Episode e "
          + "JOIN FETCH e.season s "
          + "WHERE (:seasonId IS NULL OR e.season.id = :seasonId) "
          + "AND LOWER(e.title) LIKE LOWER(CONCAT('%', :title, '%')) "
          + "AND (:episodeNum IS NULL OR e.episodeNum = :episodeNum)")
  List<Episode> findBySeasonIdAndTitleAndEpisodeNum(
      @Param("seasonId") Long seasonId,
      @Param("title") String title,
      @Param("episodeNum") Integer episodeNum);

  // Hace fetch de los actores de cada personaje en una query aparte para evitar
  // MultipleBagFetchException
  @Query("SELECT c FROM Character c " + "JOIN FETCH c.actors a")
  List<Character> findAllWithCharacters();

  // Igual que findAllWithCharacters, pero selectivo según las IDs de personaje que aparece en una
  // búsqueda específica
  @Query("SELECT c FROM Character c " + "JOIN FETCH c.actors a " + "WHERE c.id IN :ids")
  List<Character> findByIdWithCharacters(@Param("ids") List<Long> ids);
}

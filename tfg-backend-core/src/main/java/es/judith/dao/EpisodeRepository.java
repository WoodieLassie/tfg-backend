package es.judith.dao;

import es.judith.domain.Character;
import es.judith.domain.Episode;
import es.judith.domain.QEpisode;
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
  @Query("SELECT e FROM Episode e " + "LEFT JOIN FETCH e.season s")
  List<Episode> findAll();

  @Query("SELECT e FROM Episode e " + "WHERE e.id IN :ids")
  List<Episode> findAllById(List<Long> ids);

  @Query(
      "SELECT e FROM Episode e "
          + "LEFT JOIN FETCH e.season s "
          + "LEFT JOIN FETCH e.characters c WHERE e.id = :id")
  Optional<Episode> findById(@Param("id") Long id);

  @Query(
      "SELECT e FROM Episode e "
          + "LEFT JOIN FETCH e.season s "
          + "WHERE (:seasonId IS NULL OR e.season.id = :seasonId) "
          + "AND LOWER(e.title) LIKE LOWER(CONCAT('%', :title, '%')) "
          + "AND (:episodeNum IS NULL OR e.episodeNum = :episodeNum)")
  List<Episode> findBySeasonIdAndTitleAndEpisodeNum(
      @Param("seasonId") Long seasonId,
      @Param("title") String title,
      @Param("episodeNum") Integer episodeNum);

  // Hace fetch de los actores de cada personaje en una query aparte para evitar
  // MultipleBagFetchException
  @Query("SELECT c FROM Character c " + "LEFT JOIN FETCH c.actors a " + "WHERE c.id IN :ids")
  List<Character> findByIdWithCharacters(@Param("ids") List<Long> ids);
}

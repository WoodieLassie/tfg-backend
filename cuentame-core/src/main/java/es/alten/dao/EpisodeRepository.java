package es.alten.dao;

import es.alten.domain.Episode;
import es.alten.domain.QEpisode;
import es.alten.domain.Season;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
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
  @EntityGraph(attributePaths = {"season","characters"})
  List<Episode> findAll();
  @EntityGraph(attributePaths = {"season","characters"})
  Optional<Episode> findById(Long id);
  @EntityGraph(attributePaths = {"season","characters"})
  List<Episode> findBySeasonIdAndTitleAndEpisodeNum(
      @Param("seasonId") Long seasonId, @Param("title") String title, @Param("episodeNum") Integer episodeNum);
}

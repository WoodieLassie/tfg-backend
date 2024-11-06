package es.alten.dao;

import es.alten.domain.Episode;
import es.alten.domain.QEpisode;
import es.alten.domain.Season;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EpisodeRepository
    extends ElvisBaseRepository<Episode, Long, QEpisode>,
        JpaSpecificationExecutor<Episode>,
        QuerydslPredicateExecutor<Episode>,
        QuerydslBinderCustomizer<QEpisode> {
//  @Query(value = "SELECT * FROM e episodes WHERE e.season_id = :seasonId AND", nativeQuery = true)
  List<Episode> findBySeasonIdAndTitleAndEpisodeNum(
      @Param("seasonId") Long seasonId, @Param("title") String title, @Param("episodeNum") Integer episodeNum);
}

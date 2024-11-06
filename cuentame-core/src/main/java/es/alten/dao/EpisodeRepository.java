package es.alten.dao;

import es.alten.domain.Episode;
import es.alten.domain.QEpisode;
import es.alten.domain.Season;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;

public interface EpisodeRepository
    extends ElvisBaseRepository<Episode, Long, QEpisode>,
        JpaSpecificationExecutor<Episode>,
        QuerydslPredicateExecutor<Episode>,
        QuerydslBinderCustomizer<QEpisode> {
  Page<Episode> findBySeason_SeasonNumAndTitleAndEpisodeNum(
      Long seasonId, String title, Integer episodeNum, Pageable pageable);
}

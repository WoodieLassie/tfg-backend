package es.judith.dao;

import es.judith.domain.Episode;
import es.judith.domain.QSeason;
import es.judith.domain.Season;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SeasonRepository
    extends ElvisBaseRepository<Season, Long, QSeason>,
        JpaSpecificationExecutor<Season>,
        QuerydslPredicateExecutor<Season>,
        QuerydslBinderCustomizer<QSeason> {
    @Query("SELECT s FROM Season s LEFT JOIN FETCH s.episodes e WHERE s.show.id = :showId")
    List<Season> findAll(@Param("showId") Long showId);
    @Query("SELECT s FROM Season s LEFT JOIN FETCH s.episodes e WHERE s.id = :id")
    Optional<Season> findById(@Param("id") Long id);
    @Query("SELECT e FROM Episode e LEFT JOIN FETCH e.characters c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY e.id")
    List<Episode> findAllByCharacter(@Param("name") String name);
    Boolean existsBySeasonNumAndShowId(Integer seasonNum, Long showId);
}

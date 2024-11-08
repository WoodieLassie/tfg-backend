package es.alten.dao;

import es.alten.domain.QSeason;
import es.alten.domain.Season;
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
    @Query("SELECT s FROM Season s JOIN FETCH s.episodes e")
    List<Season> findAll();
    @Query("SELECT s FROM Season s JOIN FETCH s.episodes e WHERE s.id = :id")
    Optional<Season> findById(@Param("id") Long id);
}

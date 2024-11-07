package es.alten.dao;

import es.alten.domain.QSeason;
import es.alten.domain.Season;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;

import java.util.List;

public interface SeasonRepository
    extends ElvisBaseRepository<Season, Long, QSeason>,
        JpaSpecificationExecutor<Season>,
        QuerydslPredicateExecutor<Season>,
        QuerydslBinderCustomizer<QSeason> {
    @EntityGraph(attributePaths = {"episodes"})
    List<Season> findAll();
}

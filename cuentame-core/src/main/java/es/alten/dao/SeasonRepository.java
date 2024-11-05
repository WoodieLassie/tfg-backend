package es.alten.dao;

import es.alten.domain.QSeason;
import es.alten.domain.Season;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;

public interface SeasonRepository
    extends ElvisBaseRepository<Season, Long, QSeason>,
        JpaSpecificationExecutor<Season>,
        QuerydslPredicateExecutor<Season>,
        QuerydslBinderCustomizer<QSeason> {}

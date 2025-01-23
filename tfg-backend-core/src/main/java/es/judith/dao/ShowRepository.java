package es.judith.dao;

import es.judith.domain.QShow;
import es.judith.domain.Show;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;

import java.util.List;

public interface ShowRepository extends ElvisBaseRepository<Show, Long, QShow>,
        JpaSpecificationExecutor<Show>,
        QuerydslPredicateExecutor<Show>,
        QuerydslBinderCustomizer<QShow> {
}

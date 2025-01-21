package es.judith.dao;

import es.judith.domain.Actor;
import es.judith.domain.QActor;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ActorRepository
        extends ElvisBaseRepository<Actor, Long, QActor>,
        JpaSpecificationExecutor<Actor>,
        QuerydslPredicateExecutor<Actor>,
        QuerydslBinderCustomizer<QActor> {
    @Query("SELECT a from Actor a LEFT JOIN FETCH a.character c")
    List<Actor> findAll();
    @Query("SELECT a from Actor a LEFT JOIN FETCH a.character c where a.id = :id")
    Optional<Actor> findById(@Param("id") Long id);
}

package es.alten.dao;

import es.alten.domain.Actor;
import es.alten.domain.QActor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;

import java.util.List;
import java.util.Optional;

public interface ActorRepository
        extends ElvisBaseRepository<Actor, Long, QActor>,
        JpaSpecificationExecutor<Actor>,
        QuerydslPredicateExecutor<Actor>,
        QuerydslBinderCustomizer<QActor> {
    @EntityGraph(attributePaths = {"character"})
    List<Actor> findAll();
    @EntityGraph(attributePaths = {"character"})
    Optional<Actor> findById(Long id);
}

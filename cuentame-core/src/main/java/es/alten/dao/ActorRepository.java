package es.alten.dao;

import es.alten.domain.Actor;
import es.alten.domain.QActor;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;

public interface ActorRepository
        extends ElvisBaseRepository<Actor, Long, QActor>,
        JpaSpecificationExecutor<Actor>,
        QuerydslPredicateExecutor<Actor>,
        QuerydslBinderCustomizer<QActor> {}

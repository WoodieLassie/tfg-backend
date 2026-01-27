package es.judith.dao;

import es.judith.domain.Actor;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ActorRepository
        extends ElvisBaseRepository<Actor, Long>,
        JpaSpecificationExecutor<Actor> {
    @Query("SELECT a from Actor a LEFT JOIN FETCH a.character c")
    List<Actor> findAll();
    @Query("SELECT a from Actor a LEFT JOIN FETCH a.character c where a.id = :id")
    Optional<Actor> findById(@Param("id") Long id);
}

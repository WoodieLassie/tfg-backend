package es.alten.dao;

import es.alten.domain.Character;
import es.alten.domain.QCharacter;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;

import java.util.List;
import java.util.Optional;

public interface CharacterRepository
        extends ElvisBaseRepository<Character, Long, QCharacter>,
        JpaSpecificationExecutor<Character>,
        QuerydslPredicateExecutor<Character>,
        QuerydslBinderCustomizer<QCharacter> {
    @EntityGraph(attributePaths = {"actors"})
    List<Character> findAll();
    @EntityGraph(attributePaths = {"actors"})
    Optional<Character> findById(Long id);
}

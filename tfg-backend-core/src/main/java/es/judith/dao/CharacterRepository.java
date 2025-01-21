package es.judith.dao;

import es.judith.domain.Character;
import es.judith.domain.QCharacter;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CharacterRepository
        extends ElvisBaseRepository<Character, Long, QCharacter>,
        JpaSpecificationExecutor<Character>,
        QuerydslPredicateExecutor<Character>,
        QuerydslBinderCustomizer<QCharacter> {
    @Query("SELECT c from Character c LEFT JOIN FETCH c.actors a")
    List<Character> findAll();
    @Query("SELECT c from Character c LEFT JOIN FETCH c.actors a WHERE c.id = :id")
    Optional<Character> findById(@Param("id") Long id);
    @Query("SELECT c FROM Character c LEFT JOIN FETCH c.actors a WHERE c.id IN :ids")
    List<Character> findAllById(@Param("ids") List<Long> ids);
    @Modifying
    @Query(value = "DELETE FROM episode_character e WHERE e.character_id = :id", nativeQuery = true)
    void deleteFromRelatedTable(@Param("id") Long id);
}

package es.judith.dao;

import es.judith.domain.Character;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CharacterRepository
        extends GenericRepository<Character, Long>,
        JpaSpecificationExecutor<Character> {
    @Query("SELECT c from Character c LEFT JOIN FETCH c.actors a WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Character> findAllByName(@Param("name") String name);
    @Query("SELECT c from Character c LEFT JOIN FETCH c.actors a WHERE c.id = :id")
    Optional<Character> findById(@Param("id") Long id);
    @Query("SELECT c FROM Character c LEFT JOIN FETCH c.actors a WHERE c.id IN :ids")
    List<Character> findAllById(@Param("ids") List<Long> ids);
}

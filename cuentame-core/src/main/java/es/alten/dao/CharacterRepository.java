package es.alten.dao;

import es.alten.domain.Character;
import es.alten.domain.QCharacter;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;

public interface CharacterRepository
        extends ElvisBaseRepository<Character, Long, QCharacter>,
        JpaSpecificationExecutor<Character>,
        QuerydslPredicateExecutor<Character>,
        QuerydslBinderCustomizer<QCharacter> {}

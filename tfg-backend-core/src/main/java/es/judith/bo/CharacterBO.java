package es.judith.bo;

import es.judith.domain.Character;

import java.util.List;

public interface CharacterBO extends GenericBO<Character, Long> {
    List<Character> findAllByName(String name);
    List<Character> findAllById(List<Long> ids);
    byte[] findImageById(Long id);
}

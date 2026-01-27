package es.judith.bo;

import es.judith.domain.Character;

import java.util.List;

public interface CharacterBO
    extends GenericCRUDService<Character, Long> {
    List<Character> findAllById(List<Long> ids);
    byte[] findImageById(Long id);
}

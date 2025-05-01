package es.judith.bo;

import es.judith.domain.Actor;
import es.judith.domain.Character;
import es.judith.domain.QCharacter;
import es.judith.dto.CharacterFilterDTO;

import java.util.List;

public interface CharacterBO
    extends GenericCRUDService<Character, Long, QCharacter, CharacterFilterDTO> {
    List<Character> findAllById(List<Long> ids);
    byte[] findImageById(Long id);
}

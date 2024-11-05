package es.alten.bo;

import es.alten.domain.Character;
import es.alten.domain.QCharacter;
import es.alten.dto.CharacterFilterDTO;

public interface CharacterBO
    extends GenericCRUDService<Character, Long, QCharacter, CharacterFilterDTO> {}

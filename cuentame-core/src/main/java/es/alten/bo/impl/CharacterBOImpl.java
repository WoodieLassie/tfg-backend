package es.alten.bo.impl;

import es.alten.bo.CharacterBO;
import es.alten.domain.Character;
import es.alten.dao.CharacterRepository;
import es.alten.domain.QCharacter;
import es.alten.dto.CharacterFilterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CharacterBOImpl
    extends ElvisGenericCRUDServiceImpl<
        Character, Long, QCharacter, CharacterFilterDTO, CharacterRepository>
    implements CharacterBO {

    private static final long serialVersionUID = 5582874611771949151L;
    private static final Logger LOG = LoggerFactory.getLogger(CharacterBOImpl.class);

    public CharacterBOImpl(CharacterRepository repository) {
        super(repository);
    }
}

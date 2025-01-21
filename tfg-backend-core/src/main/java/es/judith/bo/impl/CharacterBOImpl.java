package es.judith.bo.impl;

import es.judith.bo.CharacterBO;
import es.judith.domain.Character;
import es.judith.dao.CharacterRepository;
import es.judith.domain.QCharacter;
import es.judith.dto.CharacterFilterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

  @Transactional(readOnly = true)
  public List<Character> findAllById(List<Long> ids) {
    LOG.debug("CharacterBOImpl: findAllById");
    return repository.findAllById(ids);
  }

  @Override
  public void delete(Long id) {
    LOG.debug("CharacterBOImpl: delete");
    repository.deleteFromRelatedTable(id);
    repository.deleteById(id);
  }
}

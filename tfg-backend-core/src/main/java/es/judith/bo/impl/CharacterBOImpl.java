package es.judith.bo.impl;

import es.judith.bo.CharacterBO;
import es.judith.domain.Character;
import es.judith.dao.CharacterRepository;
import es.judith.utils.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CharacterBOImpl
    extends ElvisGenericCRUDServiceImpl<
        Character, Long, CharacterRepository>
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
  public byte[] findImageById(Long id) {
    LOG.debug("CharacterBOImpl: findImageById");
    Optional<Character> character = repository.findById(id);
    return character.map(image -> ImageUtil.decompressImage(image.getImageData())).orElse(null);
  }

  @Override
  public void delete(Long id) {
    LOG.debug("CharacterBOImpl: delete");
    repository.deleteFromRelatedTable(id);
    repository.deleteById(id);
  }
}

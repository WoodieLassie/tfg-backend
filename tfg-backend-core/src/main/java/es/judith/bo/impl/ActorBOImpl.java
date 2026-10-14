package es.judith.bo.impl;

import es.judith.bo.ActorBO;
import es.judith.dao.ActorRepository;
import es.judith.domain.Actor;
import es.judith.exceptions.BadInputException;
import es.judith.utils.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.Serial;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ActorBOImpl extends GenericBOImpl<Actor, Long, ActorRepository> implements ActorBO {

  @Serial
  private static final long serialVersionUID = -2327250805753457217L;
  private static final Logger LOG = LoggerFactory.getLogger(ActorBOImpl.class);

  public ActorBOImpl(ActorRepository repository) {
    super(repository);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Actor> findAllByName(String name) {
    LOG.debug("ActorBOImpl: findAllByName");
    return repository.findByName(name);
  }

  @Override
  @Transactional(readOnly = true)
  public byte[] findImageById(Long id) {
    LOG.debug("ActorBOImpl: findImageById");
    Optional<Actor> actor = repository.findById(id);
    return actor.map(image -> ImageUtil.decompressImage(image.getImageData())).orElse(null);
  }

  @Override
  public String generateImageUrl(Long actorId) {
    return ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/actors/images/")
            .path(String.valueOf(actorId))
            .toUriString();
  }

  @Override
  public void throwExceptionIfIllegalStringsAreFound(String name, String nationality, String gender, String birthLocation, Date birthDate) {
    if (checkForIllegalStrings(name, 100)) {
      throw new BadInputException("Actor name cannot be greater than 100 characters");
    }
    if (checkForIllegalStrings(nationality, 100)) {
      throw new BadInputException("Actor nationality cannot be greater than 100 characters");
    }
    if (checkForIllegalStrings(gender, 100)) {
      throw new BadInputException("Actor gender cannot be greater than 100 characters");
    }
    if (checkForIllegalStrings(birthLocation, 100)) {
      throw new BadInputException("Actor birth location cannot be greater than 100 characters");
    }
    if (birthDate != null && checkForIllegalStrings(birthDate.toString(), 100)) {
      throw new BadInputException("Actor birth date cannot be greater than 100 characters");
    }
  }
}

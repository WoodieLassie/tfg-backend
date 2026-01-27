package es.judith.bo.impl;

import es.judith.bo.ActorBO;
import es.judith.dao.ActorRepository;
import es.judith.domain.Actor;
import es.judith.utils.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ActorBOImpl
    extends ElvisGenericCRUDServiceImpl<Actor, Long, ActorRepository>
    implements ActorBO {

  private static final long serialVersionUID = -2327250805753457217L;
  private static final Logger LOG = LoggerFactory.getLogger(ActorBOImpl.class);

  public ActorBOImpl(ActorRepository repository) {
    super(repository);
  }

  @Transactional(readOnly = true)
  public byte[] findImageById(Long id) {
    LOG.debug("ActorBOImpl: findImageById");
    Optional<Actor> actor = repository.findById(id);
    return actor.map(image -> ImageUtil.decompressImage(image.getImageData())).orElse(null);
  }
}

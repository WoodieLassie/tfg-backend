package es.alten.bo.impl;

import es.alten.bo.ActorBO;
import es.alten.dao.ActorRepository;
import es.alten.domain.Actor;
import es.alten.domain.QActor;
import es.alten.dto.ActorFilterDTO;
import es.alten.utils.ImageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ActorBOImpl
    extends ElvisGenericCRUDServiceImpl<Actor, Long, QActor, ActorFilterDTO, ActorRepository>
    implements ActorBO {

  private static final long serialVersionUID = -2327250805753457217L;

  public ActorBOImpl(ActorRepository repository) {
    super(repository);
  }

  public byte[] findImageById(Long id) {
    Optional<Actor> actor = repository.findById(id);
    return actor.map(image -> ImageUtil.decompressImage(image.getImageData())).orElse(null);
  }
}

package es.alten.bo;

import es.alten.domain.Actor;
import es.alten.domain.QActor;
import es.alten.dto.ActorFilterDTO;

public interface ActorBO extends GenericCRUDService<Actor, Long, QActor, ActorFilterDTO> {
    byte[] findImageById(Long id);
}

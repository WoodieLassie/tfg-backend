package es.judith.bo;

import es.judith.domain.Actor;
import es.judith.domain.QActor;
import es.judith.dto.ActorFilterDTO;

public interface ActorBO extends GenericCRUDService<Actor, Long, QActor, ActorFilterDTO> {
    byte[] findImageById(Long id);
}

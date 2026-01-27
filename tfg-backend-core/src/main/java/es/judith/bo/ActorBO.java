package es.judith.bo;

import es.judith.domain.Actor;

public interface ActorBO extends GenericCRUDService<Actor, Long> {
    byte[] findImageById(Long id);
}

package es.judith.bo;

import es.judith.domain.Actor;

import java.util.List;

public interface ActorBO extends GenericBO<Actor, Long> {
    List<Actor> findAllByName(String name);
    byte[] findImageById(Long id);
}

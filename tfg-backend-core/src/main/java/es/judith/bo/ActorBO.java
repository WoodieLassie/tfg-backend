package es.judith.bo;

import es.judith.domain.Actor;

import java.sql.Date;
import java.util.List;

public interface ActorBO extends GenericBO<Actor, Long> {
    List<Actor> findAllByName(String name);
    byte[] findImageById(Long id);
    String generateImageUrl(Long actorId);
    void throwExceptionIfIllegalStringsAreFound(String name, String nationality, String gender, String birthLocation, Date birthDate);
}

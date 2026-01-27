package es.judith.bo;

import es.judith.domain.Show;

import java.util.List;

public interface ShowBO extends GenericCRUDService<Show, Long> {
    List<Show> findAllByName(String name);
    byte[] findImageById(Long id);
}

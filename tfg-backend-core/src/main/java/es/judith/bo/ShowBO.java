package es.judith.bo;

import es.judith.domain.Show;
import es.judith.dto.ShowDTO;

import java.util.List;

public interface ShowBO extends GenericCRUDService<Show, Long> {
    List<Show> findAllByName(String name);
    byte[] findImageById(Long id);
    ShowDTO convertToDTO(Show show);
}

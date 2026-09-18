package es.judith.bo;

import es.judith.domain.Show;
import es.judith.dto.ShowDTO;

import java.util.List;

public interface ShowBO extends GenericBO<Show, Long> {
    List<Show> findAllByName(String name);
    byte[] findImageById(Long id);
    ShowDTO convertToDTO(Show show);
}

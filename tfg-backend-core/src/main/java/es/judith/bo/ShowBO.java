package es.judith.bo;

import es.judith.domain.Show;
import es.judith.dto.show.ShowDTO;
import es.judith.dto.show.ShowNoSeasonsDTO;

import java.util.List;

public interface ShowBO extends GenericBO<Show, Long> {
    List<Show> findAllByName(String name);
    byte[] findImageById(Long id);
    ShowDTO convertToShowDTO(Show show);
    ShowNoSeasonsDTO convertToShowNoSeasonsDTO(Show show);
}

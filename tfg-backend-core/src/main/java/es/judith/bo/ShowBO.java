package es.judith.bo;

import es.judith.domain.QShow;
import es.judith.domain.Show;
import es.judith.dto.ShowFilterDTO;

import java.util.List;

public interface ShowBO extends GenericCRUDService<Show, Long, QShow, ShowFilterDTO> {
    List<Show> findAllByName(String name);
}

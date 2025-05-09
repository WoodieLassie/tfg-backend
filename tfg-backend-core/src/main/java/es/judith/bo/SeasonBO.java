package es.judith.bo;

import es.judith.domain.QSeason;
import es.judith.domain.Season;
import es.judith.dto.SeasonFilterDTO;

import java.util.List;

public interface SeasonBO extends GenericCRUDService<Season, Long, QSeason, SeasonFilterDTO> {
    List<Season> findAll(Long showId);
    List<Season> findAllByCharacters(String name);
    Boolean existsBySeasonNumAndShowId(Integer seasonNum, Long showId);
}

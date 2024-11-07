package es.alten.bo;

import es.alten.domain.QSeason;
import es.alten.domain.Season;
import es.alten.dto.SeasonFilterDTO;

import java.util.List;

public interface SeasonBO extends GenericCRUDService<Season, Long, QSeason, SeasonFilterDTO>{
}

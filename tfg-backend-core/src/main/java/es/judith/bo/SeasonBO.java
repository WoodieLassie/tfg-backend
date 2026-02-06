package es.judith.bo;

import es.judith.domain.Season;

import java.util.List;

public interface SeasonBO extends GenericCRUDService<Season, Long> {
    List<Season> findAll(Long showId);
    Boolean existsBySeasonNumAndShowId(Integer seasonNum, Long showId);
}

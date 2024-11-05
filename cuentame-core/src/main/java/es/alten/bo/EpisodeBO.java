package es.alten.bo;

import es.alten.domain.Episode;
import es.alten.domain.QEpisode;
import es.alten.dto.EpisodeFilterDTO;

public interface EpisodeBO extends GenericCRUDService<Episode, Long, QEpisode, EpisodeFilterDTO> {

}

package es.alten.bo;

import es.alten.domain.Episode;
import es.alten.domain.QEpisode;
import es.alten.dto.EpisodeFilterDTO;

import java.util.List;

public interface EpisodeBO extends GenericCRUDService<Episode, Long, QEpisode, EpisodeFilterDTO> {
  List<Episode> findAll();
  Episode findOneWithCharacters(Long id);
  List<Episode> findAllSortedAndPaged(
      Long seasonId, String title, Integer episodeNum);
}

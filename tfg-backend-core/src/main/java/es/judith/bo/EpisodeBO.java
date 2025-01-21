package es.judith.bo;

import es.judith.domain.Episode;
import es.judith.domain.QEpisode;
import es.judith.dto.EpisodeFilterDTO;

import java.util.List;

public interface EpisodeBO extends GenericCRUDService<Episode, Long, QEpisode, EpisodeFilterDTO> {
  List<Episode> findAll();
  Episode findOneWithCharacters(Long id);
  List<Episode> findAllSortedAndPaged(
      Long seasonId, String title, Integer episodeNum);
}

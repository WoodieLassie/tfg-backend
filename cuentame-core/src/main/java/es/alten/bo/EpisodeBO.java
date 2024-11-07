package es.alten.bo;

import es.alten.domain.Episode;
import es.alten.domain.QEpisode;
import es.alten.domain.Season;
import es.alten.dto.EpisodeFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EpisodeBO extends GenericCRUDService<Episode, Long, QEpisode, EpisodeFilterDTO> {
  List<Episode> findAll();
  List<Episode> findAllSortedAndPaged(
      Long seasonId, String title, Integer episodeNum);
}

package es.judith.bo;

import es.judith.domain.Episode;

import java.util.List;

public interface EpisodeBO extends GenericCRUDService<Episode, Long> {
  List<Episode> findAll(Long seasonId);
  Episode findOneWithCharacters(Long id);
  List<Episode> findAllSortedAndPaged(
      Long seasonId, String title, Integer episodeNum);
}

package es.judith.bo;

import es.judith.domain.Episode;

import java.util.List;

public interface EpisodeBO extends GenericBO<Episode, Long> {
  Episode findOneWithCharacters(Long id);
  List<Episode> findAllSortedAndPaged(
      Long seasonId, String title, Integer episodeNum);
}

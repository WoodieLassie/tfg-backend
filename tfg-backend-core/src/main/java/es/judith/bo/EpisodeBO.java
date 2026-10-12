package es.judith.bo;

import es.judith.domain.Episode;
import es.judith.dto.episode.EpisodeNoSeasonDTO;

import java.util.List;

public interface EpisodeBO extends GenericBO<Episode, Long> {
  Episode findOneWithCharacters(Long id);
  List<Episode> findAllByTitle(
      String title);
  EpisodeNoSeasonDTO convertToDTO(Episode episode);
}

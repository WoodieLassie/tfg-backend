package es.judith.controller;

import es.judith.domain.Episode;
import es.judith.dto.EpisodeDTO;
import es.judith.dto.EpisodeInputDTO;
import es.judith.dto.EpisodeNoSeasonDTO;
import es.judith.rest.BaseController;
import org.springframework.http.ResponseEntity;

public interface EpisodeController extends BaseController {
  ResponseEntity<EpisodeNoSeasonDTO> findById(Long id);
  ResponseEntity<Episode> add(EpisodeInputDTO episodeDTO);
  ResponseEntity<Episode> update(Long id, EpisodeInputDTO episodeDTO);
  ResponseEntity<EpisodeDTO> delete(Long id);
}

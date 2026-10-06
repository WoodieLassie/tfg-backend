package es.judith.controller;

import es.judith.domain.Episode;
import es.judith.dto.episode.EpisodeDTO;
import es.judith.dto.episode.EpisodeInputDTO;
import es.judith.dto.episode.EpisodeNoSeasonDTO;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

public interface EpisodeController extends Serializable {
  ResponseEntity<EpisodeNoSeasonDTO> findById(Long id);
  ResponseEntity<Episode> add(EpisodeInputDTO episodeDTO);
  ResponseEntity<Episode> update(Long id, EpisodeInputDTO episodeDTO);
  ResponseEntity<EpisodeDTO> delete(Long id);
}

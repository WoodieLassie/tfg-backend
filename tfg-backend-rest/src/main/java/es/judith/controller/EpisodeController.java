package es.judith.controller;

import es.judith.domain.Episode;
import es.judith.dto.episode.EpisodeDTO;
import es.judith.dto.episode.EpisodeInputDTO;
import es.judith.dto.episode.EpisodeNoSeasonDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

public interface EpisodeController extends Serializable {
  ResponseEntity<Page<EpisodeNoSeasonDTO>> findAllByTitle(String title, Integer page, Integer size, Pageable pageable);
  ResponseEntity<EpisodeNoSeasonDTO> findById(Long id);
  ResponseEntity<Episode> add(EpisodeInputDTO episodeDTO);
  ResponseEntity<Episode> update(Long id, EpisodeInputDTO episodeDTO);
  ResponseEntity<EpisodeDTO> delete(Long id);
}

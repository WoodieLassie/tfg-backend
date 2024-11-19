package es.alten.controller;

import es.alten.domain.Episode;
import es.alten.dto.EpisodeDTO;
import es.alten.rest.BaseController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EpisodeController extends BaseController {
  ResponseEntity<List<EpisodeDTO>> findAll();
  ResponseEntity<EpisodeDTO> findById(Long id);
  ResponseEntity<Page<EpisodeDTO>> findAllSortedAndPaged(
      Long seasonId, String title, Integer episodeNum, Integer page, Pageable pageable);
  ResponseEntity<Episode> add(EpisodeDTO episodeDTO);
  ResponseEntity<Episode> update(Long id, EpisodeDTO episodeDTO);
  ResponseEntity<EpisodeDTO> delete(Long id);
}

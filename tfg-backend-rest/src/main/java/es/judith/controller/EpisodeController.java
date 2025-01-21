package es.judith.controller;

import es.judith.domain.Episode;
import es.judith.dto.EpisodeDTO;
import es.judith.dto.EpisodeInputDTO;
import es.judith.rest.BaseController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EpisodeController extends BaseController {
  ResponseEntity<List<EpisodeDTO>> findAll();
  ResponseEntity<EpisodeDTO> findById(Long id);
  ResponseEntity<Page<EpisodeDTO>> findAllSortedAndPaged(
      Long seasonId, String title, Integer episodeNum, Integer page, Pageable pageable);
  ResponseEntity<Episode> add(EpisodeInputDTO episodeDTO);
  ResponseEntity<Episode> update(Long id, EpisodeInputDTO episodeDTO);
  ResponseEntity<EpisodeDTO> delete(Long id);
}

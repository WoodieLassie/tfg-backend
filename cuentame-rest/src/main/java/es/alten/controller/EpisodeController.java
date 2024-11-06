package es.alten.controller;

import es.alten.domain.Episode;
import es.alten.dto.EpisodeDTO;
import es.alten.rest.BaseController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface EpisodeController extends BaseController {
  ResponseEntity<Page<EpisodeDTO>> findAllSortedAndPaged(
      Long seasonId, String title, Integer episodeNum, Integer page, Integer size, Pageable pageable);
}

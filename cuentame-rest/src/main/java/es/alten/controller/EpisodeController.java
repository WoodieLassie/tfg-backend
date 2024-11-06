package es.alten.controller;

import es.alten.domain.Episode;
import es.alten.rest.BaseController;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface EpisodeController extends BaseController {
  ResponseEntity<Page<Episode>> findAllSortedAndPaged(
      Long seasonNum, String title, Integer episodeNum, Integer page, Integer size);
}

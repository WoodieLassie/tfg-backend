package es.alten.controller.impl;

import es.alten.bo.EpisodeBO;
import es.alten.controller.EpisodeController;
import es.alten.domain.Episode;
import es.alten.dto.EpisodeDTO;
import es.alten.rest.impl.RestControllerImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/episodes")
@Tag(name = "episodes")
public class EpisodeControllerImpl extends RestControllerImpl<Episode, EpisodeDTO, Long, EpisodeBO>
    implements EpisodeController {

  private static final Logger LOG = LoggerFactory.getLogger(EpisodeControllerImpl.class);

  @Operation(summary = "Get element by season identification, episode title and episode number")
  @GetMapping(
          value = "/sorted",
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<EpisodeDTO>> findAllSortedAndPaged(
      @RequestParam(required = false) Long seasonId,
      @RequestParam(required = false, defaultValue = "") String title,
      @RequestParam(required = false) Integer episodeNum,
      @RequestParam(defaultValue = "0") Integer page,
      @PageableDefault(size = 5) Pageable pageable) {
    LOG.info("Fetching results with season id {} and title {} and episode number {}", seasonId, title, episodeNum);
    List<Episode> episodeList = bo.findAllSortedAndPaged(seasonId, title, episodeNum);
    List<EpisodeDTO> convertedEpisodeList = new ArrayList<>();
    for (Episode episode : episodeList) {
      EpisodeDTO episodeDTO = new EpisodeDTO();
      episodeDTO.loadFromDomain(episode);
      convertedEpisodeList.add(episodeDTO);
    }
    int start = (int)pageable.getOffset();
    int end = Math.min((start + pageable.getPageSize()), convertedEpisodeList.size());
    Page<EpisodeDTO> episodes =
        new PageImpl<>(
            convertedEpisodeList.subList(start, end), pageable, convertedEpisodeList.size());
    return ResponseEntity.ok(episodes);
  }
}

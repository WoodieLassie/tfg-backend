package es.alten.controller.impl;

import es.alten.bo.EpisodeBO;
import es.alten.bo.SeasonBO;
import es.alten.controller.EpisodeController;
import es.alten.domain.Episode;
import es.alten.domain.Season;
import es.alten.dto.EpisodeDTO;
import es.alten.dto.SeasonDTO;
import es.alten.rest.impl.RestControllerImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/episodes")
@Tag(name = "episodes")
public class EpisodeControllerImpl extends RestControllerImpl<Episode, EpisodeDTO, Long, EpisodeBO>
    implements EpisodeController {

    private final SeasonBO seasonBO;

    public EpisodeControllerImpl(SeasonBO seasonBO) {
        this.seasonBO = seasonBO;
    }

    @GetMapping(value = "/sorted/{seasonNum}", params = {"title", "episodeNum"})
    public ResponseEntity<Page<Episode>> findAllSortedAndPaged(
      @PathVariable Long seasonNum,
      @RequestParam String title,
      @RequestParam Integer episodeNum,
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "3") Integer size) {
    PageRequest pageRequest = PageRequest.of(page, size);
    Page<Episode> episodes = bo.findAllSortedAndPaged(seasonNum, title, episodeNum, pageRequest);
    return ResponseEntity.ok(episodes);
  }
}

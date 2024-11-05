package es.alten.controller.impl;

import es.alten.bo.EpisodeBO;
import es.alten.controller.EpisodeController;
import es.alten.domain.Episode;
import es.alten.dto.EpisodeDTO;
import es.alten.rest.impl.RestControllerImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/episodes")
@Tag(name = "episodes")
public class EpisodeControllerImpl extends RestControllerImpl<Episode, EpisodeDTO, Long, EpisodeBO>
    implements EpisodeController {
}

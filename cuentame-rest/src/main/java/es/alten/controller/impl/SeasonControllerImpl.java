package es.alten.controller.impl;

import es.alten.bo.SeasonBO;
import es.alten.controller.SeasonController;
import es.alten.domain.Season;
import es.alten.dto.SeasonDTO;
import es.alten.rest.impl.RestControllerImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/seasons")
@Tag(name = "seasons")
public class SeasonControllerImpl extends RestControllerImpl<Season, SeasonDTO, Long, SeasonBO>
    implements SeasonController {}

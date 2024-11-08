package es.alten.controller.impl;

import es.alten.bo.SeasonBO;
import es.alten.controller.SeasonController;
import es.alten.domain.Season;
import es.alten.dto.SeasonDTO;
import es.alten.rest.impl.RestControllerImpl;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/seasons")
@Tag(name = "seasons")
public class SeasonControllerImpl extends RestControllerImpl<Season, SeasonDTO, Long, SeasonBO>
    implements SeasonController {

  @Override
  @ApiResponses(
      value =
          @ApiResponse(
              responseCode = "200",
              description = "OK",
              content = {
                @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = SeasonDTO.class)))
              }))
  @GetMapping
  public ResponseEntity<List<SeasonDTO>> findAll() {
    return super.findAll();
  }
}

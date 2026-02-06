package es.judith.controller.impl;

import es.judith.bo.SeasonBO;
import es.judith.bo.ShowBO;
import es.judith.controller.SeasonController;
import es.judith.domain.Season;
import es.judith.domain.Show;
import es.judith.dto.SeasonDTO;
import es.judith.dto.SeasonInputDTO;
import es.judith.exceptions.AlreadyExistsException;
import es.judith.exceptions.BadInputException;
import es.judith.exceptions.NotExistingIdException;
import es.judith.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/seasons")
@Tag(name = "seasons")
public class SeasonControllerImpl implements SeasonController {

  private static final Logger LOG = LoggerFactory.getLogger(SeasonControllerImpl.class);
  private final SeasonBO bo;
  private final ShowBO showBO;

  public SeasonControllerImpl(SeasonBO bo, ShowBO showBO) {
    this.bo = bo;
    this.showBO = showBO;
  }

  @Override
  @Operation(
      method = "GET",
      summary = "Get a season by identification",
      parameters = @Parameter(ref = "id"))
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = {
        @Content(mediaType = "application/json", schema = @Schema(implementation = SeasonDTO.class))
      })
  @ApiResponse(
      responseCode = "404",
      description = "Not found",
      content = @Content(schema = @Schema(hidden = true)))
  @GetMapping("/{id}")
  public ResponseEntity<SeasonDTO> findById(@PathVariable Long id) {
    LOG.debug("SeasonControllerImpl: Fetching results with id {}", id);
    Season season = bo.findOne(id);
    if (season == null) {
      throw new NotFoundException();
    }
    SeasonDTO convertedSeason = new SeasonDTO();
    convertedSeason.loadFromDomain(season);
    return ResponseEntity.ok(convertedSeason);
  }

  @Override
  @Operation(method = "POST", summary = "Save a new season")
  @ApiResponse(
      responseCode = "201",
      description = "Created",
      content = {@Content(schema = @Schema(hidden = true))})
  @ApiResponse(
      responseCode = "409",
      description = "Conflict",
      content = {@Content(schema = @Schema(hidden = true))})
  @SecurityRequirement(name = "Authorization")
  @PostMapping
  public ResponseEntity<Season> add(@RequestBody SeasonInputDTO seasonDTO) {
    if (!seasonDTO.allFieldsArePresent()) {
      throw new BadInputException("All fields must be present in request body");
    }
    if (Boolean.TRUE.equals(
        bo.existsBySeasonNumAndShowId(seasonDTO.getSeasonNum(), seasonDTO.getShowId()))) {
      throw new AlreadyExistsException(
          "Season with number " + seasonDTO.getSeasonNum() + " already exists");
    }
    Show show = showBO.findOne(seasonDTO.getShowId());
    if (show == null) {
      throw new NotFoundException("Show with id " + seasonDTO.getShowId() + " does not exist");
    }
    Season season = seasonDTO.obtainDomainObject();
    season.setShow(show);
    LOG.debug("SeasonControllerImpl: Saving data");
    bo.save(season);
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }

  @Override
  @Operation(
      method = "PATCH",
      summary = "Edit an existing season",
      parameters = @Parameter(ref = "id"))
  @ApiResponse(
      responseCode = "204",
      description = "No content",
      content = {@Content(schema = @Schema(hidden = true))})
  @ApiResponse(
      responseCode = "404",
      description = "Not found",
      content = @Content(schema = @Schema(hidden = true)))
  @SecurityRequirement(name = "Authorization")
  @PatchMapping("/{id}")
  public ResponseEntity<Season> update(
      @PathVariable Long id, @RequestBody SeasonInputDTO seasonDTO) {
    if (!seasonDTO.allFieldsArePresent()) {
      throw new BadInputException("All fields must be present in request body");
    }
    Season newSeasonInfo = seasonDTO.obtainDomainObject();
    Season season = bo.findOne(id);
    if (season == null) {
      throw new NotExistingIdException("Actor with id " + id + " does not exist");
    }
    Show show = showBO.findOne(seasonDTO.getShowId());
    if (show == null) {
      throw new NotFoundException("Show with id " + seasonDTO.getShowId() + " does not exist");
    }
    if (Boolean.TRUE.equals(
        bo.existsBySeasonNumAndShowId(seasonDTO.getSeasonNum(), seasonDTO.getShowId())
            && !Objects.equals(seasonDTO.getShowId(), season.getShow().getId()))) {
      throw new AlreadyExistsException(
          "Season with number " + seasonDTO.getSeasonNum() + " already exists");
    }
    newSeasonInfo.setId(id);
    newSeasonInfo.setShow(show);
    LOG.debug("SeasonControllerImpl: Modifying data with id {}", id);
    bo.save(newSeasonInfo);
    return ResponseEntity.noContent().build();
  }

  @Override
  @Operation(method = "DELETE", summary = "Delete a season", parameters = @Parameter(ref = "id"))
  @ApiResponse(
      responseCode = "204",
      description = "No content",
      content = {@Content(schema = @Schema(hidden = true))})
  @ApiResponse(
      responseCode = "404",
      description = "Not found",
      content = @Content(schema = @Schema(hidden = true)))
  @SecurityRequirement(name = "Authorization")
  @DeleteMapping("/{id}")
  public ResponseEntity<SeasonDTO> delete(@PathVariable Long id) {
    if (!bo.exists(id)) {
      throw new NotFoundException("Season with id " + id + " does not exist");
    }
    LOG.debug("SeasonControllerImpl: Deleting data with id {}", id);
    bo.delete(id);
    return ResponseEntity.noContent().build();
  }
}

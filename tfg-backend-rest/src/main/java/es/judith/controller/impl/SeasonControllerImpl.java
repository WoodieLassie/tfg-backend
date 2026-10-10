package es.judith.controller.impl;

import es.judith.bo.SeasonBO;
import es.judith.bo.ShowBO;
import es.judith.controller.SeasonController;
import es.judith.domain.Season;
import es.judith.domain.Show;
import es.judith.dto.season.SeasonDTO;
import es.judith.dto.season.SeasonInputDTO;
import es.judith.exceptions.AlreadyExistsException;
import es.judith.exceptions.BadInputException;
import es.judith.exceptions.NotExistingIdException;
import es.judith.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.Serial;
import java.net.URI;
import java.util.Objects;

@RestController
@RequestMapping("/api/seasons")
@Tag(name = "seasons")
public class SeasonControllerImpl implements SeasonController {

  @Serial
  private static final long serialVersionUID = 8183016072300192421L;
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
      content = {@Content(schema = @Schema(implementation = Season.class))})
  @ApiResponse(
          responseCode = "403",
          description = "Forbidden",
          content = @Content(schema = @Schema(hidden = true)))
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
    if (bo.checkForIllegalStrings(seasonDTO.getDescription(), 100)) {
      throw new BadInputException("Season description cannot be greater than 100 characters");
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
    URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(season.getId())
            .toUri();
    return ResponseEntity.status(HttpStatus.CREATED).location(location).body(season);
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
          responseCode = "403",
          description = "Forbidden",
          content = @Content(schema = @Schema(hidden = true)))
  @ApiResponse(
      responseCode = "404",
      description = "Not found",
      content = @Content(schema = @Schema(hidden = true)))
  @SecurityRequirement(name = "Authorization")
  @PatchMapping("/{id}")
  public ResponseEntity<Season> update(
      @PathVariable Long id, @RequestBody SeasonInputDTO seasonDTO) {
    if (bo.checkForIllegalStrings(seasonDTO.getDescription(), 100)) {
      throw new BadInputException("Season description cannot be greater than 100 characters");
    }
    Season newSeasonInfo = seasonDTO.obtainDomainObject();
    Season season = bo.findOne(id);
    if (season == null) {
      throw new NotExistingIdException("Season with id " + id + " does not exist");
    }
    newSeasonInfo.setId(id);
    if (seasonDTO.getShowId() == null) {
      newSeasonInfo.setShow(season.getShow());
    }
    else {
      Show show = showBO.findOne(seasonDTO.getShowId());
      if (show == null) {
        throw new NotFoundException("Show with id " + seasonDTO.getShowId() + " does not exist");
      }
      newSeasonInfo.setShow(show);
    }
    if (newSeasonInfo.getSeasonNum() == null) {
      newSeasonInfo.setSeasonNum(season.getSeasonNum());
    }
    else {
      if (Boolean.TRUE.equals(bo.existsBySeasonNumAndShowId(seasonDTO.getSeasonNum(), seasonDTO.getShowId()))
              && !Objects.equals(seasonDTO.getShowId(), season.getShow().getId())) {
        throw new AlreadyExistsException(
                "Season with number " + seasonDTO.getSeasonNum() + " already exists");
      }
    }
    if (newSeasonInfo.getDescription() == null) {
      newSeasonInfo.setDescription(season.getDescription());
    }
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
          responseCode = "403",
          description = "Forbidden",
          content = @Content(schema = @Schema(hidden = true)))
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

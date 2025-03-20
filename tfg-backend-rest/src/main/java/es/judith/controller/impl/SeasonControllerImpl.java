package es.judith.controller.impl;

import es.judith.bo.SeasonBO;
import es.judith.controller.SeasonController;
import es.judith.domain.Season;
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

@RestController
@RequestMapping("/api/seasons")
@Tag(name = "seasons")
public class SeasonControllerImpl implements SeasonController {

  private static final Logger LOG = LoggerFactory.getLogger(SeasonControllerImpl.class);
  private final SeasonBO bo;

  public SeasonControllerImpl(SeasonBO bo) {
    this.bo = bo;
  }

  @Override
  @Operation(method = "GET", summary = "Get all seasons from show")
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = {
        @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = SeasonDTO.class)))
      })
  @GetMapping
  public ResponseEntity<List<SeasonDTO>> findAll(@Param("showId") Long showId) {
    LOG.debug("SeasonControllerImpl: Fetching all results");
    List<Season> seasonList = bo.findAll(showId);
    List<SeasonDTO> convertedSeasonList = new ArrayList<>();
    for (Season season : seasonList) {
      SeasonDTO seasonDTO = new SeasonDTO();
      seasonDTO.loadFromDomain(season);
      convertedSeasonList.add(seasonDTO);
    }
    return ResponseEntity.ok(convertedSeasonList);
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
  @Operation(method = "GET", summary = "Get all seasons by character name")
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = {
        @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = SeasonDTO.class)))
      })
  @GetMapping(value = "/sorted", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<SeasonDTO>> findAllByCharacters(
      @Parameter @RequestParam(required = false, defaultValue = "") String characterName) {
    LOG.debug("Fetching results with character name {}", characterName);
    List<Season> seasonList = bo.findAllByCharacters(characterName);
    List<SeasonDTO> convertedSeasonList = new ArrayList<>();
    for (Season season : seasonList) {
      SeasonDTO seasonDTO = new SeasonDTO();
      seasonDTO.loadFromDomain(season);
      convertedSeasonList.add(seasonDTO);
    }
    return ResponseEntity.ok(convertedSeasonList);
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
  //TODO: Revisar a que serie pertenece para tirar este error
  public ResponseEntity<Season> add(@RequestBody SeasonInputDTO seasonDTO) {
    if (!seasonDTO.allFieldsArePresent()) {
      throw new BadInputException("All fields must be present in request body");
    }
    if (Boolean.TRUE.equals(bo.existsBySeasonNum(seasonDTO.getSeasonNum()))) {
      throw new AlreadyExistsException("Season with number " + seasonDTO.getSeasonNum() + " already exists");
    }
    Season season = seasonDTO.obtainDomainObject();
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
  public ResponseEntity<Season> update(@PathVariable Long id, @RequestBody SeasonInputDTO seasonDTO) {
    if (!seasonDTO.allFieldsArePresent()) {
      throw new BadInputException("All fields must be present in request body");
    }
    if (Boolean.TRUE.equals(bo.existsBySeasonNum(seasonDTO.getSeasonNum()))) {
      throw new AlreadyExistsException("Season with number " + seasonDTO.getSeasonNum() + " already exists");
    }
    Season newSeasonInfo = seasonDTO.obtainDomainObject();
    Season season = bo.findOne(id);
    if (season == null) {
      throw new NotExistingIdException("Actor with id " + id + " does not exist");
    }
    newSeasonInfo.setId(id);
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

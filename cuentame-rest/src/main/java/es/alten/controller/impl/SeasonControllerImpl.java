package es.alten.controller.impl;

import es.alten.bo.EpisodeBO;
import es.alten.bo.SeasonBO;
import es.alten.controller.SeasonController;
import es.alten.domain.Season;
import es.alten.dto.SeasonDTO;
import es.alten.exceptions.BadInputException;
import es.alten.exceptions.NotExistingIdException;
import es.alten.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  private final EpisodeBO episodeBO;

  public SeasonControllerImpl(SeasonBO bo, EpisodeBO episodeBO) {
    this.bo = bo;
    this.episodeBO = episodeBO;
  }

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
    List<Season> seasonList = bo.findAll();
    List<SeasonDTO> convertedSeasonList = new ArrayList<>();
    for (Season season : seasonList) {
      SeasonDTO seasonDTO = new SeasonDTO();
      seasonDTO.loadFromDomain(season);
      convertedSeasonList.add(seasonDTO);
    }
    return ResponseEntity.ok(convertedSeasonList);
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<SeasonDTO> findById(@PathVariable Long id) {
    Season season = bo.findOne(id);
    if (season == null) {
      throw new NotFoundException();
    }
    SeasonDTO convertedSeason = new SeasonDTO();
    convertedSeason.loadFromDomain(season);
    return ResponseEntity.ok(convertedSeason);
  }

  @Override
  @GetMapping(value = "/sorted", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<SeasonDTO>> findAllByCharacters(
      @RequestParam(required = false, defaultValue = "") String characterName) {
    LOG.info("Fetching results with character name {}", characterName);
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
  @PostMapping
  public ResponseEntity<Season> add(@RequestBody SeasonDTO seasonDTO) {
    if (!seasonDTO.allFieldsArePresent()) {
      throw new BadInputException("All fields must be present in request body");
    }
    Season season = seasonDTO.obtainDomainObject();
    bo.save(season);
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }

  @Override
  @PatchMapping("/{id}")
  public ResponseEntity<Season> update(@PathVariable Long id, @RequestBody SeasonDTO seasonDTO) {
    if (!seasonDTO.allFieldsArePresent()) {
      throw new BadInputException("All fields must be present in request body");
    }
    Season newSeasonInfo = seasonDTO.obtainDomainObject();
    Season season = bo.findOne(id);
    if (season == null) {
      throw new NotExistingIdException("Actor with id " + id + " does not exist");
    }
    newSeasonInfo.setId(id);
    bo.save(newSeasonInfo);
    return ResponseEntity.noContent().build();
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<SeasonDTO> delete(@PathVariable Long id) {
    bo.delete(id);
    return ResponseEntity.noContent().build();
  }
}

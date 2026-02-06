package es.judith.controller.impl;

import es.judith.bo.CharacterBO;
import es.judith.bo.EpisodeBO;
import es.judith.bo.SeasonBO;
import es.judith.controller.EpisodeController;
import es.judith.domain.Character;
import es.judith.domain.Episode;
import es.judith.domain.Season;
import es.judith.dto.EpisodeDTO;
import es.judith.dto.EpisodeInputDTO;
import es.judith.dto.EpisodeNoSeasonDTO;
import es.judith.dto.EpisodeSwaggerDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/episodes")
@Tag(name = "episodes")
public class EpisodeControllerImpl implements EpisodeController {

  private static final Logger LOG = LoggerFactory.getLogger(EpisodeControllerImpl.class);
  private final EpisodeBO bo;
  private final CharacterBO characterBO;
  private final SeasonBO seasonBO;

  public EpisodeControllerImpl(EpisodeBO bo, CharacterBO characterBO, SeasonBO seasonBO) {
    this.bo = bo;
    this.characterBO = characterBO;
    this.seasonBO = seasonBO;
  }

  @Override
  @Operation(
      method = "GET",
      summary = "Get an episode by identification",
      parameters = @Parameter(ref = "id"))
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = {
        @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = EpisodeSwaggerDTO.class))
      })
  @ApiResponse(
          responseCode = "404",
          description = "Not found",
          content = @Content(schema = @Schema(hidden = true)))
  @GetMapping("/{id}")
  public ResponseEntity<EpisodeNoSeasonDTO> findById(@PathVariable Long id) {
    LOG.debug("EpisodeControllerImpl: Fetching results with id {}", id);
    if (bo.findOne(id) == null) {
      throw new NotFoundException();
    }
    Episode episode = bo.findOneWithCharacters(id);
    EpisodeNoSeasonDTO convertedEpisode = new EpisodeNoSeasonDTO();
    convertedEpisode.loadFromDomain(episode);
    return ResponseEntity.ok(convertedEpisode);
  }

  @Override
  @Operation(method = "POST", summary = "Save a new episode")
  @ApiResponse(
      responseCode = "201",
      description = "Created",
      content = {@Content(schema = @Schema(hidden = true))})
  @SecurityRequirement(name = "Authorization")
  @PostMapping
  public ResponseEntity<Episode> add(@RequestBody EpisodeInputDTO episodeDTO) {
    if (!episodeDTO.allFieldsArePresent()) {
      throw new BadInputException("All fields must be present in request body");
    }
    Episode episode = episodeDTO.obtainDomainObject();
    Season season = seasonBO.findOne(episodeDTO.getSeasonId());
    List<Long> characterIds = episodeDTO.getCharacterIds();
    List<Character> charactersInfo = characterBO.findAllById(characterIds);
    if (characterIds.size() != charactersInfo.size()) {
      throw new NotExistingIdException("Some characters provided in request body do not exist");
    }
    if (season == null) {
      throw new NotExistingIdException(
          "Season with id " + episodeDTO.getSeasonId() + " does not exist");
    }
    episode.setCharacters(charactersInfo);
    episode.setSeason(season);
    LOG.debug("EpisodeControllerImpl: Saving data");
    bo.save(episode);
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }

  @Override
  @Operation(
      method = "PATCH",
      summary = "Edit an existing episode",
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
  public ResponseEntity<Episode> update(
      @PathVariable Long id, @RequestBody EpisodeInputDTO episodeDTO) {
    if (!episodeDTO.allFieldsArePresent()) {
      throw new BadInputException("All fields must be present in request body");
    }
    Episode episode = bo.findOne(id);
    if (episode == null) {
      throw new NotExistingIdException("Episode with id " + id + " does not exist");
    }
    Episode newEpisodeInfo = episodeDTO.obtainDomainObject();
    Season season = seasonBO.findOne(episodeDTO.getSeasonId());
    List<Long> characterIds = episodeDTO.getCharacterIds();
    List<Character> charactersInfo = characterBO.findAllById(characterIds);
    if (characterIds.size() != charactersInfo.size()) {
      throw new NotExistingIdException("Some characters provided in request body do not exist");
    }
    if (season == null) {
      throw new NotExistingIdException(
          "Season with id " + episodeDTO.getSeasonId() + " does not exist");
    }
    newEpisodeInfo.setCharacters(charactersInfo);
    newEpisodeInfo.setSeason(season);
    newEpisodeInfo.setId(id);
    LOG.debug("EpisodeControllerImpl: Modifying data with id {}", id);
    bo.save(newEpisodeInfo);
    return ResponseEntity.noContent().build();
  }

  @Override
  @Operation(method = "DELETE", summary = "Delete an episode", parameters = @Parameter(ref = "id"))
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
  public ResponseEntity<EpisodeDTO> delete(@PathVariable Long id) {
    if (!bo.exists(id)) {
      throw new NotFoundException("Episode with id " + id + " does not exist");
    }
    LOG.debug("EpisodeControllerImpl: Deleting data with id {}", id);
    bo.delete(id);
    return ResponseEntity.noContent().build();
  }
}

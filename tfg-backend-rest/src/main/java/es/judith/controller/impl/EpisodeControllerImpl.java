package es.judith.controller.impl;

import es.judith.bo.CharacterBO;
import es.judith.bo.EpisodeBO;
import es.judith.bo.SeasonBO;
import es.judith.controller.EpisodeController;
import es.judith.domain.Character;
import es.judith.domain.Episode;
import es.judith.domain.Season;
import es.judith.dto.episode.EpisodeDTO;
import es.judith.dto.episode.EpisodeInputDTO;
import es.judith.dto.episode.EpisodeNoSeasonDTO;
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
import java.util.List;

@RestController
@RequestMapping("/api/episodes")
@Tag(name = "episodes")
public class EpisodeControllerImpl implements EpisodeController {

  @Serial
  private static final long serialVersionUID = -4528614600955593247L;
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
            schema = @Schema(implementation = EpisodeNoSeasonDTO.class))
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
      content = {@Content(schema = @Schema(implementation = Episode.class))})
  @ApiResponse(
          responseCode = "403",
          description = "Forbidden",
          content = @Content(schema = @Schema(hidden = true)))
  @ApiResponse(
          responseCode = "400",
          description = "Bad Request",
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
    URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(episode.getId())
            .toUri();
    return ResponseEntity.status(HttpStatus.CREATED).location(location).body(episode);
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
    Episode episode = bo.findOne(id);
    if (episode == null) {
      throw new NotExistingIdException("Episode with id " + id + " does not exist");
    }
    Episode newEpisodeInfo = episodeDTO.obtainDomainObject();

    if (episodeDTO.getCharacterIds() == null) {
      newEpisodeInfo.setCharacters(episode.getCharacters());
    }
    else {
      List<Long> characterIds = episodeDTO.getCharacterIds();
      List<Character> charactersInfo = characterBO.findAllById(characterIds);
      if (characterIds.size() != charactersInfo.size()) {
        throw new NotExistingIdException("Some characters provided in request body do not exist");
      }
      newEpisodeInfo.setCharacters(charactersInfo);
    }
    if (episodeDTO.getSeasonId() == null) {
      newEpisodeInfo.setSeason(episode.getSeason());
    }
    else {
      Season season = seasonBO.findOne(episodeDTO.getSeasonId());
      if (season == null) {
        throw new NotExistingIdException(
                "Season with id " + episodeDTO.getSeasonId() + " does not exist");
      }
      newEpisodeInfo.setSeason(season);
    }
    if (newEpisodeInfo.getEpisodeNum() == null) {
      newEpisodeInfo.setEpisodeNum(episode.getEpisodeNum());
    }
    if (newEpisodeInfo.getSummary() == null) {
      newEpisodeInfo.setSummary(episode.getSummary());
    }
    if (newEpisodeInfo.getTitle() == null) {
      newEpisodeInfo.setTitle(episode.getTitle());
    }
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

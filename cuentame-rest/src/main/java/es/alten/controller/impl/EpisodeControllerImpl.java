package es.alten.controller.impl;

import es.alten.bo.CharacterBO;
import es.alten.bo.EpisodeBO;
import es.alten.bo.SeasonBO;
import es.alten.controller.EpisodeController;
import es.alten.domain.Character;
import es.alten.domain.Episode;
import es.alten.domain.Season;
import es.alten.dto.EpisodeDTO;
import es.alten.dto.EpisodeInputDTO;
import es.alten.exceptions.BadInputException;
import es.alten.exceptions.NotExistingIdException;
import es.alten.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
  @Operation(summary = "Get all episodes")
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = {
        @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = EpisodeDTO.class)))
      })
  @GetMapping
  public ResponseEntity<List<EpisodeDTO>> findAll() {
    List<Episode> episodeList = bo.findAll();
    List<EpisodeDTO> convertedEpisodeList = new ArrayList<>();
    for (Episode episode : episodeList) {
      EpisodeDTO episodeDTO = new EpisodeDTO();
      episodeDTO.loadFromDomain(episode);
      convertedEpisodeList.add(episodeDTO);
    }
    return ResponseEntity.ok(convertedEpisodeList);
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<EpisodeDTO> findById(@PathVariable Long id) {
    Episode episode = bo.findOneWithCharacters(id);
    if (episode == null) {
      throw new NotFoundException();
    }
    EpisodeDTO convertedEpisode = new EpisodeDTO();
    convertedEpisode.loadFromDomain(episode);
    return ResponseEntity.ok(convertedEpisode);
  }

  // TODO: Cambiar Schema de este endpoint
  @Operation(summary = "Get all episodes by season identification, title and episode number")
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = {
        @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = EpisodeDTO.class)))
      })
  @GetMapping(value = "/sorted", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<EpisodeDTO>> findAllSortedAndPaged(
      @Parameter @RequestParam(required = false) Long seasonId,
      @Parameter @RequestParam(required = false, defaultValue = "") String title,
      @Parameter @RequestParam(required = false) Integer episodeNum,
      @Parameter @RequestParam(defaultValue = "0") Integer page,
      @Parameter(hidden = true) @PageableDefault(size = 5) Pageable pageable) {
    LOG.info(
        "Fetching results with season id {} and title {} and episode number {}",
        seasonId,
        title,
        episodeNum);
    List<Episode> episodeList = bo.findAllSortedAndPaged(seasonId, title, episodeNum);
    List<EpisodeDTO> convertedEpisodeList = new ArrayList<>();
    for (Episode episode : episodeList) {
      EpisodeDTO episodeDTO = new EpisodeDTO();
      episodeDTO.loadFromDomain(episode);
      convertedEpisodeList.add(episodeDTO);
    }
    int start = (int) pageable.getOffset();
    int end = Math.min((start + pageable.getPageSize()), convertedEpisodeList.size());
    Page<EpisodeDTO> episodes =
        new PageImpl<>(
            convertedEpisodeList.subList(start, end), pageable, convertedEpisodeList.size());
    return ResponseEntity.ok(episodes);
  }

  @Override
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
    bo.save(episode);
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }

  @Override
  @PatchMapping("/{id}")
  public ResponseEntity<Episode> update(@PathVariable Long id, @RequestBody EpisodeInputDTO episodeDTO) {
    if (!episodeDTO.allFieldsArePresent()) {
      throw new BadInputException("All fields must be present in request body");
    }
    Episode episode = bo.findOne(id);
    if (episode == null) {
      throw new NotExistingIdException("Episode with id " + id + " does not exist");
    }
    Episode newEpisodeInfo = episodeDTO.obtainDomainObject();
    Season season = seasonBO.findOne(episodeDTO.getSeasonId());
    List<Long> characterIds =
        episodeDTO.getCharacterIds();
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
    bo.save(newEpisodeInfo);
    return ResponseEntity.noContent().build();
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<EpisodeDTO> delete(@PathVariable Long id) {
    bo.delete(id);
    return ResponseEntity.noContent().build();
  }
}

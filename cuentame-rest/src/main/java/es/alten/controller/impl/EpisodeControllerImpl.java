package es.alten.controller.impl;

import es.alten.bo.CharacterBO;
import es.alten.bo.EpisodeBO;
import es.alten.bo.SeasonBO;
import es.alten.controller.EpisodeController;
import es.alten.domain.Character;
import es.alten.domain.Episode;
import es.alten.domain.Season;
import es.alten.dto.EpisodeDTO;
import es.alten.exceptions.BadInputException;
import es.alten.exceptions.NotExistingIdException;
import es.alten.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
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
    Episode episode = bo.findOne(id);
    if (episode == null) {
      throw new NotFoundException();
    }
    EpisodeDTO convertedEpisode = new EpisodeDTO();
    convertedEpisode.loadFromDomain(episode);
    return ResponseEntity.ok(convertedEpisode);
  }

  @Operation(summary = "Get element by season identification, episode title and episode number")
  @GetMapping(value = "/sorted", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<EpisodeDTO>> findAllSortedAndPaged(
      @RequestParam(required = false) Long seasonId,
      @RequestParam(required = false, defaultValue = "") String title,
      @RequestParam(required = false) Integer episodeNum,
      @RequestParam(defaultValue = "0") Integer page,
      @PageableDefault(size = 5) Pageable pageable) {
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
  public ResponseEntity<Episode> add(@RequestBody EpisodeDTO episodeDTO) {
    if (!episodeDTO.allFieldsArePresent()) {
      throw new BadInputException("All fields must be present in request body");
    }
    Episode episode = episodeDTO.obtainDomainObject();
    Season season = seasonBO.findOne(episode.getSeason().getId());
    List<Long> charactersIds = episode.getCharacters().stream().map(Character::getId).toList();
    List<Character> charactersInfo = characterBO.findAllById(charactersIds);
    if (episode.getCharacters().size() != charactersInfo.size()) {
      throw new NotExistingIdException("Some characters provided in request body do not exist");
    }
    if(season == null) {
      throw new NotExistingIdException("Season with id " + episode.getSeason().getId() + " does not exist");
    }
    episode.setCharacters(charactersInfo);
    episode.setSeason(season);
    bo.save(episode);
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }

  @Override
  public ResponseEntity<Episode> update(Long id, EpisodeDTO episodeDTO) {
    return null;
  }
}

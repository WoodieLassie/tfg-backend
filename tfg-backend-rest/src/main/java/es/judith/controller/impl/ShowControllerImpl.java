package es.judith.controller.impl;

import es.judith.bo.SeasonBO;
import es.judith.bo.ShowBO;
import es.judith.controller.ShowController;
import es.judith.domain.Show;
import es.judith.dto.ShowDTO;
import es.judith.dto.ShowInputDTO;
import es.judith.exceptions.BadInputException;
import es.judith.exceptions.NotExistingIdException;
import es.judith.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/shows")
@Tag(name = "shows")
public class ShowControllerImpl implements ShowController {

  private static final Logger LOG = LoggerFactory.getLogger(ShowControllerImpl.class);
  private final ShowBO bo;
  private final SeasonBO seasonBO;

  public ShowControllerImpl(ShowBO bo, SeasonBO seasonBO) {
    this.bo = bo;
    this.seasonBO = seasonBO;
  }

  @Override
  @GetMapping
  public ResponseEntity<List<ShowDTO>> findAll() {
    LOG.debug("ShowControllerImpl: Fetching all results");
    List<Show> showList = bo.findAll();
    List<ShowDTO> convertedShowList = new ArrayList<>();
    for (Show show : showList) {
      ShowDTO showDTO = new ShowDTO();
      showDTO.loadFromDomain(show);
      convertedShowList.add(showDTO);
    }
    return ResponseEntity.ok(convertedShowList);
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<ShowDTO> findById(@PathVariable Long id) {
    Show show = bo.findOne(id);
    ShowDTO showDTO = new ShowDTO();
    showDTO.loadFromDomain(show);
    return ResponseEntity.ok(showDTO);
  }

  @Override
  @GetMapping("/sorted")
  public ResponseEntity<List<ShowDTO>> findAllByName(
      @Parameter @RequestParam(defaultValue = "") String name) {
    LOG.debug("ShowControllerImpl: Fetching all results with name {}", name);
    List<Show> showList = bo.findAllByName(name);
    List<ShowDTO> convertedShowList = new ArrayList<>();
    for (Show show : showList) {
      ShowDTO showDTO = new ShowDTO();
      showDTO.loadFromDomain(show);
      convertedShowList.add(showDTO);
    }
    return ResponseEntity.ok(convertedShowList);
  }

  @Override
  @PostMapping
  public ResponseEntity<Show> add(@RequestBody ShowInputDTO showDTO) {
    if (!showDTO.allFieldsArePresent()) {
      throw new BadInputException("All fields must be present in request body");
    }
    Show show = showDTO.obtainDomainObject();
    LOG.debug("ShowControllerImpl: Saving data");
    bo.save(show);
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }

  @Override
  @PatchMapping("/{id}")
  public ResponseEntity<Show> update(@PathVariable Long id, @RequestBody ShowInputDTO showDTO) {
    if (!showDTO.allFieldsArePresent()) {
      throw new BadInputException("All fields must be present in request body");
    }
    Show newShowInfo = showDTO.obtainDomainObject();
    Show show = bo.findOne(id);
    if (show == null) {
      throw new NotExistingIdException("Show with id " + id + " does not exist");
    }
    newShowInfo.setId(id);
    LOG.debug("ShowControllerImpl: Modifying data with id {}", id);
    bo.save(newShowInfo);
    return ResponseEntity.noContent().build();
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<Show> delete(@PathVariable Long id) {
    if (!bo.exists(id)) {
      throw new NotFoundException("Show with id " + id + " does not exist");
    }
    LOG.debug("ShowControllerImpl: Deleting data with id {}", id);
    seasonBO.delete(seasonBO.findAll(id));
    bo.delete(id);
    return ResponseEntity.noContent().build();
  }
}

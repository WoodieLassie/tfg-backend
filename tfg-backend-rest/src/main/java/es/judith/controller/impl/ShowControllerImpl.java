package es.judith.controller.impl;

import es.judith.bo.SeasonBO;
import es.judith.bo.ShowBO;
import es.judith.controller.ShowController;
import es.judith.domain.Actor;
import es.judith.domain.Character;
import es.judith.domain.Show;
import es.judith.dto.ShowDTO;
import es.judith.dto.ShowInputDTO;
import es.judith.exceptions.BadInputException;
import es.judith.exceptions.NotExistingIdException;
import es.judith.exceptions.NotFoundException;
import es.judith.utils.ImageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
      if (showDTO.getImageData() != null) {
        String characterImageDownloadURL =
            ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/shows/images/")
                .path(String.valueOf(showDTO.getId()))
                .toUriString();
        showDTO.setImageUrl(characterImageDownloadURL);
      }
    }
    return ResponseEntity.ok(convertedShowList);
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<ShowDTO> findById(@PathVariable Long id) {
    Show show = bo.findOne(id);
    if (show == null) {
      throw new NotFoundException();
    }
    ShowDTO showDTO = new ShowDTO();
    showDTO.loadFromDomain(show);
    if (showDTO.getImageData() != null) {
      String characterImageDownloadURL =
          ServletUriComponentsBuilder.fromCurrentContextPath()
              .path("/shows/images/")
              .path(String.valueOf(showDTO.getId()))
              .toUriString();
      showDTO.setImageUrl(characterImageDownloadURL);
    }
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
  @Operation(
      method = "GET",
      summary = "Get a show image by show identification",
      parameters = @Parameter(ref = "id"))
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = {@Content(mediaType = "image/png", schema = @Schema(hidden = true))})
  @ApiResponse(
      responseCode = "404",
      description = "Not found",
      content = @Content(schema = @Schema(hidden = true)))
  @GetMapping(value = "/image/{id}", produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity<byte[]> findImageById(@PathVariable Long id) {
    LOG.debug("ShowControllerImpl: Fetching image results with character id {}", id);
    byte[] image = bo.findImageById(id);
    if (image == null || image.length == 0) {
      throw new NotFoundException();
    }
    return ResponseEntity.ok(image);
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
  @Operation(
      method = "PATCH",
      summary = "Edit an existing show image",
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
  @PatchMapping(value = "/image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Show> updateImageById(
      @PathVariable Long id, @RequestParam("image") MultipartFile file) {
    if (file.getSize() == 0) {
      throw new BadInputException("A file must be attached to request");
    }
    if (!Objects.equals(file.getContentType(), "image/png")
        && !Objects.equals(file.getContentType(), "image/jpeg")) {
      throw new BadInputException("File must be png or jpg");
    }
    Show show = bo.findOne(id);
    if (show == null) {
      throw new NotExistingIdException("Show with id " + id + " does not exist");
    }
    try {
      show.setImageData(ImageUtil.compressImage(file.getBytes()));
    } catch (IOException e) {
      throw new BadInputException(e);
    }
    LOG.debug("ShowControllerImpl: Modifying image data with show id {}", id);
    bo.save(show);
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

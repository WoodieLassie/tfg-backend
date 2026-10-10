package es.judith.controller.impl;

import es.judith.bo.SeasonBO;
import es.judith.bo.ShowBO;
import es.judith.controller.ShowController;
import es.judith.domain.Show;
import es.judith.dto.show.ShowDTO;
import es.judith.dto.show.ShowInputDTO;
import es.judith.dto.show.ShowNoSeasonsDTO;
import es.judith.dto.swagger.FriendSwaggerDTO;
import es.judith.exceptions.BadInputException;
import es.judith.exceptions.NotExistingIdException;
import es.judith.exceptions.NotFoundException;
import es.judith.utils.ImageUtil;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.Serial;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/shows")
@Tag(name = "shows")
public class ShowControllerImpl implements ShowController {

  @Serial
  private static final long serialVersionUID = 509791301070857618L;
  private static final Logger LOG = LoggerFactory.getLogger(ShowControllerImpl.class);
  private final ShowBO bo;
  private final SeasonBO seasonBO;

  public ShowControllerImpl(ShowBO bo, SeasonBO seasonBO) {
    this.bo = bo;
    this.seasonBO = seasonBO;
  }

  @Override
  @Operation(
          method = "GET",
          summary = "Get all shows")
  @ApiResponse(
          responseCode = "200",
          description = "OK",
          content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ShowNoSeasonsDTO.class))))
  @GetMapping
  public ResponseEntity<List<ShowNoSeasonsDTO>> findAll(
      @Parameter @RequestParam(defaultValue = "") String name) {
    if (Objects.equals(name, "")) {
      LOG.debug("ShowControllerImpl: Fetching all results");
      List<Show> showList = bo.findAll();
      List<ShowNoSeasonsDTO> convertedShowList = new ArrayList<>();
      for (Show show : showList) {
        ShowNoSeasonsDTO showDTO = bo.convertToShowNoSeasonsDTO(show);
        convertedShowList.add(showDTO);
      }
      return ResponseEntity.ok(convertedShowList);
    }
    LOG.debug("ShowControllerImpl: Fetching all results with name {}", name);
    List<Show> showList = bo.findAllByName(name);
    List<ShowNoSeasonsDTO> convertedShowList = new ArrayList<>();
    for (Show show : showList) {
      ShowNoSeasonsDTO showDTO = bo.convertToShowNoSeasonsDTO(show);
      if (showDTO.getImageData() != null) {
        String showImageDownloadUrl =
                ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/shows/image/")
                        .path(String.valueOf(showDTO.getId()))
                        .toUriString();
        showDTO.setImageUrl(showImageDownloadUrl);
      }
      convertedShowList.add(showDTO);
    }
    return ResponseEntity.ok(convertedShowList);
  }

  @Override
  @Operation(
          method = "GET",
          summary = "Get a show by show ID",
          parameters = @Parameter(ref = "id"))
  @ApiResponse(
          responseCode = "200",
          description = "OK",
          content = {@Content(schema = @Schema(implementation = ShowDTO.class))})
  @ApiResponse(
          responseCode = "404",
          description = "Not found",
          content = @Content(schema = @Schema(hidden = true)))
  @GetMapping("/{id}")
  public ResponseEntity<ShowDTO> findById(@PathVariable Long id) {
    Show show = bo.findOne(id);
    if (show == null) {
      throw new NotFoundException();
    }
    ShowDTO showDTO = bo.convertToShowDTO(show);
    if (showDTO.getImageData() != null) {
      String showImageDownloadUrl =
              ServletUriComponentsBuilder.fromCurrentContextPath()
                      .path("/api/shows/image/")
                      .path(String.valueOf(showDTO.getId()))
                      .toUriString();
      showDTO.setImageUrl(showImageDownloadUrl);
    }
    return ResponseEntity.ok(showDTO);
  }

  @Override
  @Operation(
      method = "GET",
      summary = "Get a show image by show ID",
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
  @Operation(method = "POST", summary = "Save a new show")
  @ApiResponse(
          responseCode = "201",
          description = "Created",
          content = {@Content(schema = @Schema(implementation = Show.class))})
  @ApiResponse(
          responseCode = "403",
          description = "Forbidden",
          content = @Content(schema = @Schema(hidden = true)))
  @ApiResponse(
          responseCode = "409",
          description = "Conflict",
          content = {@Content(schema = @Schema(hidden = true))})
  @ApiResponse(
          responseCode = "400",
          description = "Bad Request",
          content = {@Content(schema = @Schema(hidden = true))})
  @SecurityRequirement(name = "Authorization")
  @PostMapping
  public ResponseEntity<Show> add(@RequestBody ShowInputDTO showDTO) {
    if (!showDTO.allFieldsArePresent()) {
      throw new BadInputException("All fields must be present in request body");
    }
    Show show = showDTO.obtainDomainObject();
    LOG.debug("ShowControllerImpl: Saving data");
    bo.save(show);
    URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(show.getId())
            .toUri();
    return ResponseEntity.status(HttpStatus.CREATED).location(location).body(show);
  }

  @Override
  @Operation(method = "PATCH", summary = "Edit show")
  @ApiResponse(
          responseCode = "204",
          description = "No Content",
          content = {@Content(schema = @Schema(hidden = true))})
  @ApiResponse(
          responseCode = "403",
          description = "Forbidden",
          content = @Content(schema = @Schema(hidden = true)))
  @ApiResponse(
          responseCode = "409",
          description = "Conflict",
          content = {@Content(schema = @Schema(hidden = true))})
  @ApiResponse(
          responseCode = "400",
          description = "Bad Request",
          content = {@Content(schema = @Schema(hidden = true))})
  @SecurityRequirement(name = "Authorization")
  @PatchMapping("/{id}")
  public ResponseEntity<Show> update(@PathVariable Long id, @RequestBody ShowInputDTO showDTO) {
    Show newShowInfo = showDTO.obtainDomainObject();
    Show show = bo.findOne(id);
    if (show == null) {
      throw new NotExistingIdException("Show with id " + id + " does not exist");
    }
    LOG.debug("ShowControllerImpl: Modifying data with id {}", id);
    newShowInfo.setId(id);
    if (newShowInfo.getName() == null) {
      newShowInfo.setName(show.getName());
    }
    if (newShowInfo.getDescription() == null) {
      newShowInfo.setDescription(show.getDescription());
    }
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
          responseCode = "403",
          description = "Forbidden",
          content = @Content(schema = @Schema(hidden = true)))
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
  @Operation(
          method = "DELETE",
          summary = "Delete show")
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
          description = "Not Found",
          content = @Content(schema = @Schema(hidden = true)))
  @SecurityRequirement(name = "Authorization")
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

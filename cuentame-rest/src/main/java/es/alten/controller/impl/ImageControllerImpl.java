package es.alten.controller.impl;

import es.alten.bo.ImageBO;
import es.alten.controller.ImageController;
import es.alten.domain.Image;
import es.alten.dto.ImageDTO;
import es.alten.exceptions.BadInputException;
import es.alten.exceptions.NotExistingIdException;
import es.alten.exceptions.NotFoundException;
import es.alten.utils.ImageUtil;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/images")
@Tag(name = "images")
public class ImageControllerImpl implements ImageController {
  private static final Logger LOG = LoggerFactory.getLogger(ImageControllerImpl.class);
  private final ImageBO bo;

  public ImageControllerImpl(ImageBO bo) {
    this.bo = bo;
  }

  @Override
  @Operation(method = "GET", summary = "Get all images")
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = {
        @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = ImageDTO.class)))
      })
  @GetMapping
  public ResponseEntity<List<ImageDTO>> findAll() {
    List<Image> images = bo.findAll();
    List<ImageDTO> convertedImages = new ArrayList<>();
    for (Image image : images) {
      ImageDTO imageDTO = new ImageDTO();
      imageDTO.loadFromDomain(image);
      String imageDownloadUrl =
          ServletUriComponentsBuilder.fromCurrentContextPath()
              .path("/api/images/")
              .path(String.valueOf(imageDTO.getId()))
              .toUriString();
      imageDTO.setImageUrl(imageDownloadUrl);
      convertedImages.add(imageDTO);
    }
    return ResponseEntity.ok(convertedImages);
  }

  @Override
  @Operation(method = "GET", summary = "Get all images by name")
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = {
        @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = ImageDTO.class)))
      })
  @GetMapping(value = "/sorted", params = {"name"})
  public ResponseEntity<List<ImageDTO>> findByName(@Parameter @RequestParam String name) {
    List<Image> images = bo.findByName(name);
    List<ImageDTO> convertedImages = new ArrayList<>();
    for (Image image : images) {
      ImageDTO imageDTO = new ImageDTO();
      imageDTO.loadFromDomain(image);
      String imageDownloadUrl =
          ServletUriComponentsBuilder.fromCurrentContextPath()
              .path("/api/images/")
              .path(String.valueOf(imageDTO.getId()))
              .toUriString();
      imageDTO.setImageUrl(imageDownloadUrl);
      convertedImages.add(imageDTO);
    }
    return ResponseEntity.ok(convertedImages);
  }

  @Override
  @Operation(
      method = "GET",
      summary = "Get an image by identification",
      parameters = @Parameter(ref = "id"))
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = {
        @Content(mediaType = "image/png", schema = @Schema(hidden = true))
      })
  @ApiResponse(
      responseCode = "404",
      description = "Not found",
      content = @Content(schema = @Schema(hidden = true)))
  @GetMapping(produces = MediaType.IMAGE_PNG_VALUE, value = "/{id}")
  public ResponseEntity<byte[]> findById(@PathVariable Long id) {
    byte[] imageData = bo.findById(id);
    if (imageData == null || imageData.length == 0) {
      throw new NotFoundException();
    }
    return ResponseEntity.ok(imageData);
  }

  @Override
  @Operation(method = "POST", summary = "Save a new image")
  @ApiResponse(
          responseCode = "201",
          description = "Created",
          content = {@Content(schema = @Schema(hidden = true))})
  @SecurityRequirement(name = "Authorization")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Image> add(@RequestParam("image") MultipartFile file) {
    if (file.getSize() == 0) {
      throw new BadInputException("A file must be attached to request");
    }
    if (!Objects.equals(file.getContentType(), "image/png")
        && !Objects.equals(file.getContentType(), "image/jpeg")) {
      throw new BadInputException("File must be png or jpg");
    }
    ImageDTO imageDTO = new ImageDTO();
    imageDTO.setName(file.getOriginalFilename());
    imageDTO.setType(file.getContentType());
    try {
      imageDTO.setImageData(ImageUtil.compressImage(file.getBytes()));
    } catch (IOException e) {
      throw new BadInputException(e);
    }
    bo.save(imageDTO.obtainDomainObject());
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }

  @Override
  @Operation(method = "PATCH", summary = "Edit an existing image", parameters = @Parameter(ref = "id"))
  @ApiResponse(
          responseCode = "204",
          description = "No content",
          content = {@Content(schema = @Schema(hidden = true))})
  @ApiResponse(
          responseCode = "404",
          description = "Not found",
          content = @Content(schema = @Schema(hidden = true)))
  @SecurityRequirement(name = "Authorization")
  @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Image> update(
      @PathVariable Long id, @RequestParam("image") MultipartFile file) {
    if (file.getSize() == 0) {
      throw new BadInputException("A file must be attached to request");
    }
    if (!Objects.equals(file.getContentType(), "image/png")
        && !Objects.equals(file.getContentType(), "image/jpeg")) {
      throw new BadInputException("File must be png or jpg");
    }
    Image image = bo.findOne(id);
    if (image == null) {
      throw new NotExistingIdException("Character with id " + id + " does not exist");
    }
    ImageDTO imageDTO = new ImageDTO();
    imageDTO.setName(file.getOriginalFilename());
    imageDTO.setType(file.getContentType());
    try {
      imageDTO.setImageData(ImageUtil.compressImage(file.getBytes()));
    } catch (IOException e) {
      throw new BadInputException(e);
    }
    Image newImageInfo = imageDTO.obtainDomainObject();
    newImageInfo.setId(id);
    bo.save(newImageInfo);
    return ResponseEntity.noContent().build();
  }

  @Override
  @Operation(method = "DELETE", summary = "Delete an image", parameters = @Parameter(ref = "id"))
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
  public ResponseEntity<ImageDTO> delete(@PathVariable Long id) {
    bo.delete(id);
    return ResponseEntity.noContent().build();
  }
}

package es.judith.controller.impl;

import es.judith.bo.CharacterBO;
import es.judith.controller.CharacterController;
import es.judith.domain.Actor;
import es.judith.domain.Character;
import es.judith.dto.ActorNoCharacterDTO;
import es.judith.dto.CharacterDTO;
import es.judith.dto.CharacterInputDTO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/characters")
@Tag(name = "characters")
public class CharacterControllerImpl implements CharacterController {

  private static final Logger LOG = LoggerFactory.getLogger(CharacterControllerImpl.class);
  private final CharacterBO bo;

  public CharacterControllerImpl(CharacterBO bo) {
    this.bo = bo;
  }

  @Override
  @Operation(method = "GET", summary = "Get all characters")
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = {
        @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = CharacterDTO.class)))
      })
  @GetMapping
  public ResponseEntity<List<CharacterDTO>> findAll() {
    LOG.debug("CharacterControllerImpl: Fetching all results");
    List<Character> characterList = bo.findAll();
    List<CharacterDTO> convertedCharacterList = new ArrayList<>();
    for (Character character : characterList) {
      CharacterDTO characterDTO = new CharacterDTO();
      characterDTO.loadFromDomain(character);
      for (ActorNoCharacterDTO actor : characterDTO.getActors()) {
        if (actor.getImageData() != null) {
          String actorImageDownloadUrl =
              ServletUriComponentsBuilder.fromCurrentContextPath()
                  .path("/actors/images/")
                  .path(String.valueOf(actor.getId()))
                  .toUriString();
          actor.setImageUrl(actorImageDownloadUrl);
        }
      }
      convertedCharacterList.add(characterDTO);
    }
    return ResponseEntity.ok(convertedCharacterList);
  }

  @Override
  @Operation(
      method = "GET",
      summary = "Get a character by identification",
      parameters = @Parameter(ref = "id"))
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = {
        @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = CharacterDTO.class))
      })
  @ApiResponse(
      responseCode = "404",
      description = "Not found",
      content = @Content(schema = @Schema(hidden = true)))
  @GetMapping("/{id}")
  public ResponseEntity<CharacterDTO> findById(@PathVariable Long id) {
    LOG.debug("CharacterControllerImpl: Fetching results with id {}", id);
    Character character = bo.findOne(id);
    if (character == null) {
      throw new NotFoundException();
    }
    CharacterDTO convertedCharacter = new CharacterDTO();
    convertedCharacter.loadFromDomain(character);
    for (ActorNoCharacterDTO actor : convertedCharacter.getActors()) {
      if (actor.getImageData() != null) {
        String actorImageDownloadUrl =
            ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/actors/images/")
                .path(String.valueOf(actor.getId()))
                .toUriString();
        actor.setImageUrl(actorImageDownloadUrl);
      }
    }
    return ResponseEntity.ok(convertedCharacter);
  }

  @Override
  @Operation(
          method = "GET",
          summary = "Get a character image by character identification",
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
    LOG.debug("CharacterControllerImpl: Fetching image results with character id {}", id);
    byte[] image = bo.findImageById(id);
    if (image == null || image.length == 0) {
      throw new NotFoundException();
    }
    return ResponseEntity.ok(image);
  }


  @Override
  @Operation(method = "POST", summary = "Save a new character")
  @ApiResponse(
      responseCode = "201",
      description = "Created",
      content = {@Content(schema = @Schema(hidden = true))})
  @SecurityRequirement(name = "Authorization")
  @PostMapping
  public ResponseEntity<Character> add(@RequestBody CharacterInputDTO characterDTO) {
    if (!characterDTO.allFieldsArePresent()) {
      throw new BadInputException("All fields must be present in request body");
    }
    Character character = characterDTO.obtainDomainObject();
    LOG.debug("CharacterControllerImpl: Saving data");
    bo.save(character);
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }

  @Override
  @Operation(
      method = "PATCH",
      summary = "Edit an existing character",
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
  public ResponseEntity<Character> update(
      @PathVariable Long id, @RequestBody CharacterInputDTO characterDTO) {
    if (!characterDTO.allFieldsArePresent()) {
      throw new BadInputException("All fields must be present in request body");
    }
    Character newCharacterInfo = characterDTO.obtainDomainObject();
    Character character = bo.findOne(id);
    if (character == null) {
      throw new NotExistingIdException("Character with id " + id + " does not exist");
    }
    newCharacterInfo.setId(character.getId());
    LOG.debug("CharacterControllerImpl: Modifying data with id {}", id);
    bo.save(newCharacterInfo);
    return ResponseEntity.noContent().build();
  }

  @Override
  @Operation(
          method = "PATCH",
          summary = "Edit an existing character image",
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
  public ResponseEntity<Actor> updateImageById(
          @PathVariable Long id, @RequestParam("image") MultipartFile file) {
    if (file.getSize() == 0) {
      throw new BadInputException("A file must be attached to request");
    }
    if (!Objects.equals(file.getContentType(), "image/png")
            && !Objects.equals(file.getContentType(), "image/jpeg")) {
      throw new BadInputException("File must be png or jpg");
    }
    Character character = bo.findOne(id);
    if (character == null) {
      throw new NotExistingIdException("Actor with id " + id + " does not exist");
    }
    try {
      character.setImageData(ImageUtil.compressImage(file.getBytes()));
    } catch (IOException e) {
      throw new BadInputException(e);
    }
    LOG.debug("ActorControllerImpl: Modifying image data with actor id {}", id);
    bo.save(character);
    return ResponseEntity.noContent().build();
  }

  @Override
  @Operation(method = "DELETE", summary = "Delete a character", parameters = @Parameter(ref = "id"))
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
  public ResponseEntity<CharacterDTO> delete(@PathVariable Long id) {
    if (!bo.exists(id)) {
      throw new NotFoundException("Character with id " + id + " does not exist");
    }
    LOG.debug("CharacterControllerImpl: Deleting data with id {}", id);
    bo.delete(id);
    return ResponseEntity.noContent().build();
  }
}

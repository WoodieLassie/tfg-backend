package es.alten.controller.impl;

import es.alten.bo.ActorBO;
import es.alten.bo.CharacterBO;
import es.alten.controller.ActorController;
import es.alten.domain.Actor;
import es.alten.domain.Character;
import es.alten.dto.ActorDTO;
import es.alten.dto.ActorInputDTO;
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
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/actors")
@Tag(name = "actors")
public class ActorControllerImpl implements ActorController {
  private final ActorBO bo;
  private final CharacterBO characterBO;

  public ActorControllerImpl(ActorBO bo, CharacterBO characterBO) {
    this.bo = bo;
    this.characterBO = characterBO;
  }

  @Override
  @Operation(method = "GET", summary = "Get all actors")
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = {
        @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = ActorDTO.class)))
      })
  @GetMapping
  public ResponseEntity<List<ActorDTO>> findAll() {
    List<Actor> actors = bo.findAll();
    List<ActorDTO> convertedActors = new ArrayList<>();
    for (Actor actor : actors) {
      ActorDTO actorDTO = new ActorDTO();
      actorDTO.loadFromDomain(actor);
      if (actorDTO.getImageData() != null) {
        String actorImageDownloadUrl =
            ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/actors/images/")
                .path(String.valueOf(actorDTO.getId()))
                .toUriString();
        actorDTO.setImageUrl(actorImageDownloadUrl);
      }
      convertedActors.add(actorDTO);
    }
    return ResponseEntity.ok(convertedActors);
  }

  @Override
  @Operation(
      method = "GET",
      summary = "Get an actor by identification",
      parameters = @Parameter(ref = "id"))
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = {
        @Content(mediaType = "application/json", schema = @Schema(implementation = ActorDTO.class))
      })
  @ApiResponse(
      responseCode = "404",
      description = "Not found",
      content = @Content(schema = @Schema(hidden = true)))
  @GetMapping("/{id}")
  public ResponseEntity<ActorDTO> findById(@PathVariable Long id) {
    Actor actor = bo.findOne(id);
    if (actor == null) {
      throw new NotFoundException();
    }
    ActorDTO convertedActor = new ActorDTO();
    convertedActor.loadFromDomain(actor);
    if (convertedActor.getImageData() != null) {
      String actorImageDownloadUrl =
          ServletUriComponentsBuilder.fromCurrentContextPath()
              .path("/api/actors/images/")
              .path(String.valueOf(convertedActor.getId()))
              .toUriString();
      convertedActor.setImageUrl(actorImageDownloadUrl);
    }
    return ResponseEntity.ok(convertedActor);
  }

  @Override
  @Operation(
      method = "GET",
      summary = "Get an actor image by actor identification",
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
    byte[] image = bo.findImageById(id);
    if (image == null || image.length == 0) {
      throw new NotFoundException();
    }
    return ResponseEntity.ok(image);
  }

  @Override
  @PostMapping
  public ResponseEntity<Actor> add(@RequestBody ActorInputDTO actorDTO) {
    if (!actorDTO.allFieldsArePresent()) {
      throw new BadInputException("All fields must be present in request body");
    }
    Actor actor = actorDTO.obtainDomainObject();
    Character character = characterBO.findOne(actorDTO.getCharacterId());
    actor.setCharacter(character);
    bo.save(actor);
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }

  @Override
  @PatchMapping("/{id}")
  public ResponseEntity<Actor> update(@PathVariable Long id, @RequestBody ActorInputDTO actorDTO) {
    if (!actorDTO.allFieldsArePresent()) {
      throw new BadInputException("All fields must be present in request body");
    }
    Actor newActorInfo = actorDTO.obtainDomainObject();
    Actor actor = bo.findOne(id);
    if (actor == null) {
      throw new NotExistingIdException("Actor with id " + id + " does not exist");
    }
    Character character = characterBO.findOne(actorDTO.getCharacterId());
    if (character == null) {
      throw new NotExistingIdException(
          "Character with id " + actorDTO.getCharacterId() + " does not exist");
    }
    newActorInfo.setCharacter(character);
    newActorInfo.setId(id);
    bo.save(newActorInfo);
    return ResponseEntity.noContent().build();
  }

  @Override
  @PatchMapping(value = "/image/{id}")
  public ResponseEntity<Actor> updateImageById(
      @PathVariable Long id, @RequestParam("image") MultipartFile file) {
    if (file.getSize() == 0) {
      throw new BadInputException("A file must be attached to request");
    }
    if (!Objects.equals(file.getContentType(), "image/png")
        && !Objects.equals(file.getContentType(), "image/jpeg")) {
      throw new BadInputException("File must be png or jpg");
    }
    Actor actor = bo.findOne(id);
    if (actor == null) {
      throw new NotExistingIdException("Actor with id " + id + " does not exist");
    }
    try {
      actor.setImageData(ImageUtil.compressImage(file.getBytes()));
    } catch (IOException e) {
      throw new BadInputException(e);
    }
    bo.save(actor);
    return ResponseEntity.noContent().build();
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<ActorDTO> delete(@PathVariable Long id) {
    bo.delete(id);
    return ResponseEntity.noContent().build();
  }
}

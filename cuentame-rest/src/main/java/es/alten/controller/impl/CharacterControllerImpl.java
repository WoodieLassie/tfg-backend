package es.alten.controller.impl;

import es.alten.bo.CharacterBO;
import es.alten.controller.CharacterController;
import es.alten.domain.Character;
import es.alten.dto.ActorNoCharacterDTO;
import es.alten.dto.CharacterDTO;
import es.alten.dto.CharacterInputDTO;
import es.alten.exceptions.BadInputException;
import es.alten.exceptions.NotExistingIdException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/characters")
@Tag(name = "characters")
public class CharacterControllerImpl implements CharacterController {

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
    Character character = bo.findOne(id);
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
    bo.save(newCharacterInfo);
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
    bo.delete(id);
    return ResponseEntity.noContent().build();
  }
}

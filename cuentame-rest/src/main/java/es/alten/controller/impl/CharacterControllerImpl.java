package es.alten.controller.impl;

import es.alten.bo.CharacterBO;
import es.alten.controller.CharacterController;
import es.alten.domain.Actor;
import es.alten.domain.Character;
import es.alten.dto.ActorNoCharacterDTO;
import es.alten.dto.CharacterDTO;
import es.alten.exceptions.BadInputException;
import es.alten.exceptions.NotExistingIdException;
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
  @GetMapping("/{id}")
  public ResponseEntity<CharacterDTO> findOne(@PathVariable Long id) {
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
  @PostMapping
  public ResponseEntity<Character> add(@RequestBody CharacterDTO characterDTO) {
    if (!characterDTO.allFieldsArePresent()) {
      throw new BadInputException("All fields must be present in request body");
    }
    Character character = characterDTO.obtainDomainObject();
    bo.save(character);
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }

  @Override
  @PatchMapping("/{id}")
  public ResponseEntity<Character> update(
      @PathVariable Long id, @RequestBody CharacterDTO characterDTO) {
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
}

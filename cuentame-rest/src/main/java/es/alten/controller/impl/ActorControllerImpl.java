package es.alten.controller.impl;

import es.alten.bo.ActorBO;
import es.alten.bo.CharacterBO;
import es.alten.controller.ActorController;
import es.alten.domain.Actor;
import es.alten.domain.Character;
import es.alten.dto.ActorDTO;
import es.alten.exceptions.BadInputException;
import es.alten.utils.ImageUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
  @GetMapping
  public ResponseEntity<List<ActorDTO>> findAll() {
    List<Actor> actors = bo.findAll();
    List<ActorDTO> convertedActors = new ArrayList<>();
    for (Actor actor : actors) {
      ActorDTO actorDTO = new ActorDTO();
      actorDTO.loadFromDomain(actor);
      convertedActors.add(actorDTO);
    }
    return ResponseEntity.ok(convertedActors);
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<ActorDTO> findById(@PathVariable Long id) {
    Actor actor = bo.findOne(id);
    ActorDTO convertedActor = new ActorDTO();
    convertedActor.loadFromDomain(actor);
    return ResponseEntity.ok(convertedActor);
  }

  @Override
  @GetMapping(value = "/image/{id}", produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity<byte[]> findImageById(@PathVariable Long id) {
    return ResponseEntity.ok(bo.findImageById(id));
  }

  @Override
  @PostMapping
  public ResponseEntity<ActorDTO> add(@RequestBody Actor actor) {
    Character character = characterBO.findOne(actor.getCharacter().getId());
    actor.setCharacter(character);
    actor.setImageData(null);
    bo.save(actor);
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }

  @Override
  @PatchMapping(value = "/image/{id}")
  public ResponseEntity<ActorDTO> updateImageById(
      @PathVariable Long id, @RequestParam("image") MultipartFile file) {
    // TODO: Validar tipos de formato de archivo permitidos (jpg, png). Hacer lo mismo en
    // controlador de images
    Actor actor = bo.findOne(id);
    try {
      actor.setImageData(ImageUtil.compressImage(file.getBytes()));
    } catch (IOException e) {
      throw new BadInputException(e);
    }
    bo.save(actor);
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }
}

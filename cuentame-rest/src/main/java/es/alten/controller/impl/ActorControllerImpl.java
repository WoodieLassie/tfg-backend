package es.alten.controller.impl;

import es.alten.bo.ActorBO;
import es.alten.controller.ActorController;
import es.alten.domain.Actor;
import es.alten.dto.ActorDTO;
import es.alten.exceptions.BadInputException;
import es.alten.rest.impl.RestControllerImpl;
import es.alten.utils.ImageUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/actors")
@Tag(name = "actors")
public class ActorControllerImpl extends RestControllerImpl<Actor, ActorDTO, Long, ActorBO>
    implements ActorController {
  @Override
  @GetMapping(value = "/image/{id}", produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity<byte[]> findImageById(@PathVariable Long id) {
    return ResponseEntity.ok(bo.findImageById(id));
  }

  @PatchMapping(value = "/image/{id}")
  public ResponseEntity<ActorDTO> updateImageById(
      @PathVariable Long id, @RequestParam("image") MultipartFile file) {
    //TODO: Validar tipos de formato de archivo permitidos (jpg, png). Hacer lo mismo en controlador de images
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

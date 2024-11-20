package es.alten.controller;

import es.alten.domain.Actor;
import es.alten.dto.ActorDTO;
import es.alten.dto.ActorInputDTO;
import es.alten.rest.BaseController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ActorController extends BaseController {
    ResponseEntity<List<ActorDTO>> findAll();
    ResponseEntity<ActorDTO> findById(Long id);
    ResponseEntity<byte[]> findImageById(Long id);
    ResponseEntity<Actor> update(Long id, ActorInputDTO actorDTO);
    ResponseEntity<Actor> updateImageById(Long id, MultipartFile file);
    ResponseEntity<Actor> add(ActorInputDTO actorDTO);
    ResponseEntity<ActorDTO> delete(Long id);
}

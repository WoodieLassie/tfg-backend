package es.alten.controller;

import es.alten.domain.Actor;
import es.alten.dto.ActorDTO;
import es.alten.rest.BaseController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ActorController extends BaseController {
    ResponseEntity<List<ActorDTO>> findAll();
    ResponseEntity<ActorDTO> findById(Long id);
    ResponseEntity<byte[]> findImageById(Long id);
    ResponseEntity<ActorDTO> updateImageById(Long id, MultipartFile file);
    ResponseEntity<Actor> add(ActorDTO actorDTO);
}

package es.judith.controller;

import es.judith.domain.Actor;
import es.judith.dto.ActorDTO;
import es.judith.dto.ActorInputDTO;
import es.judith.rest.BaseController;
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

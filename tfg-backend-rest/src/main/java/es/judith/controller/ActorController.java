package es.judith.controller;

import es.judith.domain.Actor;
import es.judith.dto.actor.ActorDTO;
import es.judith.dto.actor.ActorInputDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

public interface ActorController extends Serializable {
    ResponseEntity<Page<ActorDTO>> findAll(Integer page, Integer size, Pageable pageable);
    ResponseEntity<ActorDTO> findById(Long id);
    ResponseEntity<byte[]> findImageById(Long id);
    ResponseEntity<Actor> update(Long id, ActorInputDTO actorDTO);
    ResponseEntity<Actor> updateImageById(Long id, MultipartFile file);
    ResponseEntity<Actor> add(ActorInputDTO actorDTO);
    ResponseEntity<ActorDTO> delete(Long id);
}

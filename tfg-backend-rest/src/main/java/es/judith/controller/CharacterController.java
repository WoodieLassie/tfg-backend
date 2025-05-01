package es.judith.controller;

import es.judith.domain.Actor;
import es.judith.dto.CharacterDTO;
import es.judith.domain.Character;
import es.judith.dto.CharacterInputDTO;
import es.judith.web.BaseController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CharacterController extends BaseController {
    ResponseEntity<List<CharacterDTO>> findAll();
    ResponseEntity<CharacterDTO> findById(Long id);
    ResponseEntity<byte[]> findImageById(Long id);
    ResponseEntity<Character> add(CharacterInputDTO characterDTO);
    ResponseEntity<Character> update(Long id, CharacterInputDTO characterDTO);
    ResponseEntity<Actor> updateImageById(Long id, MultipartFile file);
    ResponseEntity<CharacterDTO> delete(Long id);
}

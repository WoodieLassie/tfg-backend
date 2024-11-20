package es.alten.controller;

import es.alten.dto.CharacterDTO;
import es.alten.domain.Character;
import es.alten.dto.CharacterInputDTO;
import es.alten.web.BaseController;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CharacterController extends BaseController {
    ResponseEntity<List<CharacterDTO>> findAll();
    ResponseEntity<CharacterDTO> findById(Long id);
    ResponseEntity<Character> add(CharacterInputDTO characterDTO);
    ResponseEntity<Character> update(Long id, CharacterInputDTO characterDTO);
    ResponseEntity<CharacterDTO> delete(Long id);
}

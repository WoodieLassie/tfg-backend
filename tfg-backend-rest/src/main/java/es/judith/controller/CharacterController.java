package es.judith.controller;

import es.judith.dto.CharacterDTO;
import es.judith.domain.Character;
import es.judith.dto.CharacterInputDTO;
import es.judith.web.BaseController;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CharacterController extends BaseController {
    ResponseEntity<List<CharacterDTO>> findAll();
    ResponseEntity<CharacterDTO> findById(Long id);
    ResponseEntity<Character> add(CharacterInputDTO characterDTO);
    ResponseEntity<Character> update(Long id, CharacterInputDTO characterDTO);
    ResponseEntity<CharacterDTO> delete(Long id);
}

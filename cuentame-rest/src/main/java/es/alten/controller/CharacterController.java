package es.alten.controller;

import es.alten.dto.CharacterDTO;
import es.alten.domain.Character;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CharacterController {
    ResponseEntity<List<CharacterDTO>> findAll();
    ResponseEntity<CharacterDTO> findOne(Long id);
    ResponseEntity<Character> add(CharacterDTO characterDTO);
    ResponseEntity<Character> update(Long id, CharacterDTO characterDTO);
}

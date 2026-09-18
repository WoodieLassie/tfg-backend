package es.judith.controller;

import es.judith.dto.CharacterDTO;
import es.judith.domain.Character;
import es.judith.dto.CharacterInputDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

public interface CharacterController extends Serializable {
    ResponseEntity<List<CharacterDTO>> findAll();
    ResponseEntity<CharacterDTO> findById(Long id);
    ResponseEntity<byte[]> findImageById(Long id);
    ResponseEntity<Character> add(CharacterInputDTO characterDTO);
    ResponseEntity<Character> update(Long id, CharacterInputDTO characterDTO);
    ResponseEntity<Character> updateImageById(Long id, MultipartFile file);
    ResponseEntity<CharacterDTO> delete(Long id);
}

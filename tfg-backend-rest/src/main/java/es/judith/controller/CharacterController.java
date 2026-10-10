package es.judith.controller;

import es.judith.dto.character.CharacterDTO;
import es.judith.domain.Character;
import es.judith.dto.character.CharacterInputDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

public interface CharacterController extends Serializable {
    ResponseEntity<Page<CharacterDTO>> findAll(Integer page, Integer size, Pageable pageable);
    ResponseEntity<CharacterDTO> findById(Long id);
    ResponseEntity<byte[]> findImageById(Long id);
    ResponseEntity<Character> add(CharacterInputDTO characterDTO);
    ResponseEntity<Character> update(Long id, CharacterInputDTO characterDTO);
    ResponseEntity<Character> updateImageById(Long id, MultipartFile file);
    ResponseEntity<CharacterDTO> delete(Long id);
}

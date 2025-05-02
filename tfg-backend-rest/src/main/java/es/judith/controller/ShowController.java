package es.judith.controller;

import es.judith.domain.Actor;
import es.judith.domain.Show;
import es.judith.dto.ShowDTO;
import es.judith.dto.ShowInputDTO;
import es.judith.web.BaseController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ShowController extends BaseController {
    ResponseEntity<List<ShowDTO>> findAll();
    ResponseEntity<ShowDTO> findById(Long id);
    ResponseEntity<byte[]> findImageById(Long id);
    ResponseEntity<List<ShowDTO>> findAllByName(String name);
    ResponseEntity<Show> add(ShowInputDTO showDTO);
    ResponseEntity<Show> update(Long id, ShowInputDTO showDTO);
    ResponseEntity<Show> updateImageById(Long id, MultipartFile file);
    ResponseEntity<Show> delete(Long id);
}

package es.judith.controller;

import es.judith.domain.Show;
import es.judith.dto.show.ShowDTO;
import es.judith.dto.show.ShowInputDTO;
import es.judith.dto.show.ShowNoSeasonsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

public interface ShowController extends Serializable {
    ResponseEntity<Page<ShowNoSeasonsDTO>> findAll(String name, Integer page, Integer size, Pageable pageable);
    ResponseEntity<ShowDTO> findById(Long id);
    ResponseEntity<byte[]> findImageById(Long id);
    ResponseEntity<Show> add(ShowInputDTO showDTO);
    ResponseEntity<Show> update(Long id, ShowInputDTO showDTO);
    ResponseEntity<Show> updateImageById(Long id, MultipartFile file);
    ResponseEntity<Show> delete(Long id);
}

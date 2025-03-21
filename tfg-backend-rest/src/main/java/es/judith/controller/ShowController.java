package es.judith.controller;

import es.judith.domain.Show;
import es.judith.dto.ShowDTO;
import es.judith.dto.ShowInputDTO;
import es.judith.web.BaseController;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ShowController extends BaseController {
    ResponseEntity<List<ShowDTO>> findAll();
    ResponseEntity<List<ShowDTO>> findAllByName(String name);
    ResponseEntity<Show> add(ShowInputDTO showDTO);
    ResponseEntity<Show> update(Long id, ShowInputDTO showDTO);
    ResponseEntity<Show> delete(Long id);
}

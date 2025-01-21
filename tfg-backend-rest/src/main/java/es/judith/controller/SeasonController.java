package es.judith.controller;

import es.judith.domain.Season;
import es.judith.dto.SeasonDTO;
import es.judith.dto.SeasonInputDTO;
import es.judith.rest.BaseController;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SeasonController extends BaseController {
    ResponseEntity<List<SeasonDTO>> findAll();
    ResponseEntity<SeasonDTO> findById(Long id);
    ResponseEntity<List<SeasonDTO>> findAllByCharacters(String characterName);
    ResponseEntity<Season> add(SeasonInputDTO seasonDTO);
    ResponseEntity<Season> update(Long id, SeasonInputDTO seasonDTO);
    ResponseEntity<SeasonDTO> delete(Long id);
}

package es.alten.controller;

import es.alten.domain.Season;
import es.alten.dto.SeasonDTO;
import es.alten.rest.BaseController;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SeasonController extends BaseController {
    ResponseEntity<List<SeasonDTO>> findAll();
    ResponseEntity<SeasonDTO> findById(Long id);
    ResponseEntity<List<SeasonDTO>> findAllByCharacters(String characterName);
    ResponseEntity<Season> add(SeasonDTO seasonDTO);
    ResponseEntity<Season> update(Long id, SeasonDTO seasonDTO);
    ResponseEntity<SeasonDTO> delete(Long id);
}

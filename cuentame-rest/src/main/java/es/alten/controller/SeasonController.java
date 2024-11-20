package es.alten.controller;

import es.alten.domain.Season;
import es.alten.dto.SeasonDTO;
import es.alten.dto.SeasonInputDTO;
import es.alten.rest.BaseController;
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

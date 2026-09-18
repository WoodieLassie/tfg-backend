package es.judith.controller;

import es.judith.domain.Season;
import es.judith.dto.SeasonDTO;
import es.judith.dto.SeasonInputDTO;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

public interface SeasonController extends Serializable {
    ResponseEntity<SeasonDTO> findById(Long id);
    ResponseEntity<Season> add(SeasonInputDTO seasonDTO);
    ResponseEntity<Season> update(Long id, SeasonInputDTO seasonDTO);
    ResponseEntity<SeasonDTO> delete(Long id);
}

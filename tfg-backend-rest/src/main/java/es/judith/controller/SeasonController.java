package es.judith.controller;

import es.judith.domain.Season;
import es.judith.dto.season.SeasonDTO;
import es.judith.dto.season.SeasonInputDTO;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

public interface SeasonController extends Serializable {
    ResponseEntity<SeasonDTO> findById(Long id);
    ResponseEntity<Season> add(SeasonInputDTO seasonDTO);
    ResponseEntity<Season> update(Long id, SeasonInputDTO seasonDTO);
    ResponseEntity<SeasonDTO> delete(Long id);
}

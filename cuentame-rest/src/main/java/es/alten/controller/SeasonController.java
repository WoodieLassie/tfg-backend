package es.alten.controller;

import es.alten.dto.SeasonDTO;
import es.alten.rest.BaseController;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SeasonController extends BaseController {
    ResponseEntity<List<SeasonDTO>> findAllByCharacters(String characterName);
}

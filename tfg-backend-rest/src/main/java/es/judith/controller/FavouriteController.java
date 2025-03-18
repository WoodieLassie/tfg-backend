package es.judith.controller;

import es.judith.domain.Favourite;
import es.judith.dto.FavouriteDTO;
import es.judith.dto.FavouriteInputDTO;
import es.judith.rest.BaseController;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FavouriteController extends BaseController {
    ResponseEntity<List<FavouriteDTO>> findAllByUser(Long userId);
    ResponseEntity<Favourite> add(FavouriteInputDTO favouriteDTO);
    ResponseEntity<FavouriteDTO> delete(Long id);
}

package es.judith.controller;

import es.judith.domain.Favourite;
import es.judith.dto.FavouriteDTO;
import es.judith.dto.FavouriteInputDTO;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.List;

public interface FavouriteController extends Serializable {
    ResponseEntity<List<FavouriteDTO>> findAllByUser(Long userId);
    ResponseEntity<Favourite> add(FavouriteInputDTO favouriteDTO);
    ResponseEntity<FavouriteDTO> delete(Long id);
}

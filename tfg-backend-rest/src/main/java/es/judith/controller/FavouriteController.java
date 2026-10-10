package es.judith.controller;

import es.judith.domain.Favourite;
import es.judith.dto.favourite.FavouriteDTO;
import es.judith.dto.favourite.FavouriteInputDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.List;

public interface FavouriteController extends Serializable {
    ResponseEntity<Page<FavouriteDTO>> findAllByUser(Long userId, Integer page, Integer size, Pageable pageable);
    ResponseEntity<Favourite> add(FavouriteInputDTO favouriteDTO);
    ResponseEntity<FavouriteDTO> delete(Long id);
}

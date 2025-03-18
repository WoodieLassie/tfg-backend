package es.judith.bo;

import es.judith.domain.Favourite;
import es.judith.domain.QFavourite;
import es.judith.dto.FavouriteFilterDTO;

import java.util.List;

public interface FavouriteBO extends GenericCRUDService<Favourite, Long, QFavourite, FavouriteFilterDTO>{
    List<Favourite> findAllByUser(Long userId);
}

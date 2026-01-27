package es.judith.bo;

import es.judith.domain.Favourite;

import java.util.List;

public interface FavouriteBO extends GenericCRUDService<Favourite, Long>{
    List<Favourite> findAllByUser(Long userId);
}

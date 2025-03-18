package es.judith.dto;

import es.judith.domain.Favourite;
import org.springframework.data.jpa.domain.Specification;

public class FavouriteFilterDTO extends BaseFilterDTO<Favourite>{
    @Override
    public Specification<Favourite> obtainFilterSpecification() {
        return null;
    }
}

package es.alten.dto;

import es.alten.domain.Image;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.criteria.Predicate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@Schema(name = "ImageFilterDTO", description = "Image filter")
@EqualsAndHashCode(callSuper = true)
@Data
public class ImageFilterDTO extends BaseFilterDTO<Image>{
    @Serial private static final long serialVersionUID = -5657218039586348064L;

    private String name;
    private String type;
    private byte[] imageData;

    @Override
    public Specification<Image> obtainFilterSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (name != null ) {
                predicates.add(cb.like(root.get("name"), "%" + this.name + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

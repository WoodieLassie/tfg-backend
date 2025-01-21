package es.judith.dto;

import es.judith.domain.Actor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.Specification;

@Schema(name = "ActorFilterDTO", description = "Actor filter")
@EqualsAndHashCode(callSuper = true)
@Data
public class ActorFilterDTO extends BaseFilterDTO<Actor> {
    private static final long serialVersionUID = 5403701927000727599L;

    @Override
    public Specification obtainFilterSpecification() {
        return null;
    }
}

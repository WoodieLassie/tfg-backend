package es.judith.dto;

import es.judith.domain.Character;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serial;

@Schema(name = "CharacterFilterDTO", description = "Character filter")
@EqualsAndHashCode(callSuper = true)
@Data
public class CharacterFilterDTO extends BaseFilterDTO<Character> {
    @Serial private static final long serialVersionUID = 1150266437393001412L;

    @Override
    public Specification obtainFilterSpecification() {
        return null;
    }
}

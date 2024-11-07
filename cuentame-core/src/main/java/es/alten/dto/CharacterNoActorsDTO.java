package es.alten.dto;

import es.alten.domain.Character;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(name = "CharacterDTO", description = "Data transfer object. Character without actor data")
@EqualsAndHashCode(callSuper = true)
@Data
public class CharacterNoActorsDTO extends ElvisBaseDTO<Character> {
  @NotNull private String name;
  @NotNull private String description;
  @NotNull private String gender;
  @NotNull private String nationality;
  @NotNull private Integer age;
}

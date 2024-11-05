package es.alten.dto;

import es.alten.domain.Character;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

@Schema(name = "SeasonDTO", description = "Data transfer object. Character")
@EqualsAndHashCode(callSuper = true)
@Data
public class CharacterDTO extends ElvisBaseDTO<Character> {
  @Serial private static final long serialVersionUID = -7112078588141267975L;

  @NotNull private String name;
  @NotNull private String description;
  @NotNull private String gender;
  @NotNull private String nationality;
  @NotNull private Integer age;
  private List<ActorDTO> actors;
}

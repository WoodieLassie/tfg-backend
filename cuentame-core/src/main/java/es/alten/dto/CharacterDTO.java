package es.alten.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
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

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonView(View.ShowActor.class)
  private List<ActorDTO> actors;
}

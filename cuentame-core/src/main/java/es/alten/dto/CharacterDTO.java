package es.alten.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import es.alten.domain.Character;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Schema(name = "CharacterDTO", description = "Data transfer object. Character")
@EqualsAndHashCode(callSuper = true)
@Data
public class CharacterDTO extends ElvisBaseDTO<Character> {
  @Serial private static final long serialVersionUID = -7112078588141267975L;

  @NotNull private String name;
  @NotNull private String description;
  @NotNull private String gender;
  @NotNull private String nationality;
  @NotNull private Integer age;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private List<ActorNoCharacterDTO> actors;

  public boolean allFieldsArePresent() {
    return Stream.of(this.name, this.description, this.gender, this.nationality, this.age)
        .allMatch(Objects::nonNull);
  }
}

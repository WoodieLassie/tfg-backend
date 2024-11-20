package es.alten.dto;

import es.alten.domain.Character;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Schema(name = "CharacterInputDTO", description = "Data transfer object for input. Character")
@EqualsAndHashCode(callSuper = true)
@Data
public class CharacterInputDTO extends ElvisBaseDTO<Character> {
  @Schema(description = "Character name")
  @NotNull private String name;
  @Schema(description = "Character description")
  @NotNull private String description;
  @Schema(description = "Character gender")
  @NotNull private String gender;
  @Schema(description = "Character nationality")
  @NotNull private String nationality;
  @Schema(description = "Character age")
  @NotNull private Integer age;

  public boolean allFieldsArePresent() {
    return Stream.of(this.name, this.description, this.gender, this.nationality, this.age)
            .allMatch(Objects::nonNull);
  }
}

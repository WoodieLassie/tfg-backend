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
  @NotNull private String name;
  @NotNull private String description;
  @NotNull private String gender;
  @NotNull private String nationality;
  @NotNull private Integer age;

  public boolean allFieldsArePresent() {
    return Stream.of(this.name, this.description, this.gender, this.nationality, this.age)
            .allMatch(Objects::nonNull);
  }
}

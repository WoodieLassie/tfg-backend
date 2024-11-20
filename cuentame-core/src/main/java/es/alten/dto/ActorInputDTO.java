package es.alten.dto;

import es.alten.domain.Actor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;
import java.util.Objects;
import java.util.stream.Stream;

@Schema(name = "ActorInputDTO", description = "Data transfer object for input. Actor")
@EqualsAndHashCode(callSuper = true)
@Data
public class ActorInputDTO extends ElvisBaseDTO<Actor> {
  @NotNull private String name;
  @NotNull private Date birthDate;
  @NotNull private String nationality;
  @NotNull private String gender;
  @NotNull private String birthLocation;
  @NotNull private Long characterId;

  public boolean allFieldsArePresent() {
    return Stream.of(
                    this.name,
                    this.birthDate,
                    this.nationality,
                    this.gender,
                    this.birthLocation,
                    this.characterId)
            .allMatch(Objects::nonNull);
  }
}

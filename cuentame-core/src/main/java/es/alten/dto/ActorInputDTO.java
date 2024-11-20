package es.alten.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
  private static final long serialVersionUID = -6545076495785074106L;

  @JsonIgnore private Long id;

  @Schema(description = "Actor name")
  @NotNull
  private String name;

  @Schema(description = "Actor birth date")
  @NotNull
  private Date birthDate;

  @Schema(description = "Actor nationality")
  @NotNull
  private String nationality;

  @Schema(description = "Actor gender")
  @NotNull
  private String gender;

  @Schema(description = "Actor birth location")
  @NotNull
  private String birthLocation;

  @Schema(description = "Identification of the character that this actor interprets")
  @NotNull
  private Long characterId;

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

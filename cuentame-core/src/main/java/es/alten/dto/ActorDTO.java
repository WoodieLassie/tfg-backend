package es.alten.dto;

import es.alten.domain.Actor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.sql.Date;

@Schema(name = "ActorDTO", description = "Data transfer object. Actor")
@EqualsAndHashCode(callSuper = true)
@Data
public class ActorDTO extends ElvisBaseDTO<Actor> {
  @Serial private static final long serialVersionUID = -7933845043962093551L;

  @NotNull private String name;
  @NotNull private Date birthDate;
  @NotNull private String nationality;
  @NotNull private String gender;
  @NotNull private String birthLocation;

  @NotNull
  private CharacterNoActorsDTO character;

  private byte[] imageData;
}

package es.alten.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import es.alten.domain.Actor;
import es.alten.utils.View;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.sql.Date;

@Schema(name = "SeasonDTO", description = "Data transfer object. Actor")
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
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonView(View.ShowCharacter.class)
  private CharacterDTO character;
}

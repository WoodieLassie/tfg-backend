package es.judith.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import es.judith.domain.Actor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;

@Schema(name = "ActorNoCharacterDTO", description = "Data transfer object. Actor")
@EqualsAndHashCode(callSuper = true)
@Data
public class ActorNoCharacterDTO extends ElvisBaseDTO<Actor> {
  @NotNull private String name;
  @NotNull private Date birthDate;
  @NotNull private String nationality;
  @NotNull private String gender;
  @NotNull private String birthLocation;
  @JsonIgnore private byte[] imageData;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  private String imageUrl;
}

package es.judith.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import es.judith.domain.Character;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(
    name = "CharacterNoActorsDTO",
    description = "Data transfer object. Character without actor data")
@EqualsAndHashCode(callSuper = true)
@Data
public class CharacterNoActorsDTO extends ElvisBaseDTO<Character> {
  @NotNull private Long id;
  @NotNull private String name;
  @NotNull private String description;
  @NotNull private String gender;
  @NotNull private String nationality;
  @NotNull private Integer age;
  @JsonIgnore private byte[] imageData;

  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  private String imageUrl;
}

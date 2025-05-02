package es.judith.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import es.judith.domain.Show;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Schema(name = "ShowDTO", description = "Data transfer object. Show")
@EqualsAndHashCode(callSuper = true)
@Data
public class ShowDTO extends ElvisBaseDTO<Show> {
  @Serial private static final long serialVersionUID = -4692305723930464000L;

  @NotNull private String name;
  @NotNull private String description;

  @JsonIgnore
  private byte[] imageData;

  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  private String imageUrl;
}

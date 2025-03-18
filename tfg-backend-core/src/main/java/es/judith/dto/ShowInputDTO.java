package es.judith.dto;

import es.judith.domain.Show;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Schema(name = "ShowInputDTO", description = "Data transfer object for input. Show")
@EqualsAndHashCode(callSuper = true)
@Data
public class ShowInputDTO extends ElvisBaseDTO<Show> {
  @Serial private static final long serialVersionUID = -6353858445451047313L;

  @NotNull private String name;
  @NotNull private String description;
}

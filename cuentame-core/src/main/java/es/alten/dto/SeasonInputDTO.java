package es.alten.dto;

import es.alten.domain.Season;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(name = "SeasonInputDTO", description = "Data transfer object for input. Season")
@EqualsAndHashCode(callSuper = true)
@Data
public class SeasonInputDTO extends ElvisBaseDTO<Season> {
  @Schema(description = "Season number")
  @NotNull
  private Integer seasonNum;

  @Schema(description = "Season description")
  @NotNull
  private String description;
}

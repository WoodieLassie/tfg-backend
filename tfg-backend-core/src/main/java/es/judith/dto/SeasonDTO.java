package es.judith.dto;

import es.judith.domain.Season;
import es.judith.domain.Show;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

@Schema(name = "SeasonDTO", description = "Data transfer object. Season")
@EqualsAndHashCode(callSuper = true)
@Data
public class SeasonDTO extends ElvisBaseDTO<Season> {
  @Serial private static final long serialVersionUID = -8617926782001985439L;

  @Schema(description = "Season identification")
  @NotNull
  private Long id;

  @Schema(description = "Season number")
  @NotNull
  private Integer seasonNum;

  @Schema(description = "Season description")
  @NotNull
  private String description;

  @Schema(description = "Season episodes")
  private List<EpisodeNoSeasonDTO> episodes;

  @Schema(description = "Season show")
  @NotNull
  private ShowDTO show;
}

package es.judith.dto;

import es.judith.domain.Season;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Schema(
    name = "SeasonNoEpisodesDTO",
    description = "Data transfer object. Season without episode data")
@EqualsAndHashCode(callSuper = true)
@Data
public class SeasonNoEpisodesDTO extends ElvisBaseDTO<Season> {
  @Serial private static final long serialVersionUID = -8617926782001985439L;

  @NotNull private Long id;
  @NotNull private Integer seasonNum;
  @NotNull private String description;
}

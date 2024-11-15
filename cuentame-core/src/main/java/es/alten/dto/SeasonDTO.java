package es.alten.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import es.alten.domain.Season;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Schema(name = "SeasonDTO", description = "Data transfer object. Season")
@EqualsAndHashCode(callSuper = true)
@Data
public class SeasonDTO extends ElvisBaseDTO<Season> {
  @Serial private static final long serialVersionUID = -8617926782001985439L;

  @Schema(description = "Season number")
  @NotNull private Integer seasonNum;
  @Schema(description = "Season description")
  @NotNull private String description;

  @Schema(description = "Season episodes")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private List<EpisodeNoSeasonDTO> episodes;

  public boolean allFieldsArePresent() {
    return Stream.of(
                    this.seasonNum,
                    this.description)
            .allMatch(Objects::nonNull);
  }
}

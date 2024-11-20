package es.alten.dto;

import es.alten.domain.Episode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Schema(name = "EpisodeInputDTO", description = "Data transfer object for input. Episode")
@EqualsAndHashCode(callSuper = true)
@Data
public class EpisodeInputDTO extends ElvisBaseDTO<Episode> {
  @Schema(description = "Episode number")
  @NotNull
  private Integer episodeNum;

  @Schema(description = "Episode title")
  @NotNull
  private String title;

  @Schema(description = "Episode summary")
  @NotNull
  private String summary;

  @Schema(description = "Episode season identification")
  @NotNull
  private Long seasonId;

  @Schema(description = "Character identifications")
  private List<Long> characterIds;

  public boolean allFieldsArePresent() {
    return Stream.of(this.episodeNum, this.title, this.summary, this.seasonId, this.characterIds)
            .allMatch(Objects::nonNull);
  }
}

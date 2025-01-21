package es.judith.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import es.judith.domain.Episode;
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
  private static final long serialVersionUID = -4157249428593446209L;

  @JsonIgnore private Long id;

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

  @Schema(description = "Identifications of the characters that appear in this episode")
  private List<Long> characterIds;

  public boolean allFieldsArePresent() {
    return Stream.of(this.episodeNum, this.title, this.summary, this.seasonId, this.characterIds)
        .allMatch(Objects::nonNull);
  }
}

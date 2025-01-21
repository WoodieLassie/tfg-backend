package es.judith.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import es.judith.domain.Episode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

@Schema(name = "EpisodeDTO", description = "Data transfer object. Episode")
@EqualsAndHashCode(callSuper = true)
@Data
public class EpisodeDTO extends ElvisBaseDTO<Episode> {
  @Serial private static final long serialVersionUID = 5170483706615630449L;

  @Schema(description = "Episode identification")
  @NotNull
  private Long id;

  @Schema(description = "Episode number")
  @NotNull
  private Integer episodeNum;

  @Schema(description = "Episode title")
  @NotNull
  private String title;

  @Schema(description = "Episode summary")
  @NotNull
  private String summary;

  @Schema(description = "Episode season information")
  @NotNull
  private SeasonNoEpisodesDTO season;

  @Schema(hidden = true)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @NotNull
  private List<CharacterDTO> characters;
}

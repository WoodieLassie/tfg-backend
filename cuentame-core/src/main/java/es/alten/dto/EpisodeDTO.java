package es.alten.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import es.alten.domain.Episode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

@Schema(name = "SeasonDTO", description = "Data transfer object. Episode")
@EqualsAndHashCode(callSuper = true)
@Data
public class EpisodeDTO extends ElvisBaseDTO<Episode> {
  @Serial private static final long serialVersionUID = 5170483706615630449L;

  @NotNull private Integer episodeNum;
  @NotNull private String title;
  @NotNull private String summary;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @NotNull
  private SeasonDTO season;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @NotNull
  private List<CharacterDTO> characters;
}

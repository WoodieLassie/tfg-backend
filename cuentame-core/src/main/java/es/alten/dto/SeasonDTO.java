package es.alten.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import es.alten.domain.Season;
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

  @NotNull private Integer seasonNum;
  @NotNull private String description;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @NotNull
  @JsonView({View.ShowEpisode.class})
  private List<EpisodeDTO> episodes;
}

package es.alten.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import es.alten.domain.Episode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

@Schema(name = "EpisodeNoSeasonDTO", description = "Data transfer object. Episode without season info")
@EqualsAndHashCode(callSuper = true)
@Data
public class EpisodeNoSeasonDTO extends ElvisBaseDTO<Episode>{
    @Serial
    private static final long serialVersionUID = -637798618800399514L;

    @NotNull
    private Integer episodeNum;
    @NotNull private String title;
    @NotNull private String summary;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private List<CharacterDTO> characters;
}

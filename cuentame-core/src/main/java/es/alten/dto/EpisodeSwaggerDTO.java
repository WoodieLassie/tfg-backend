package es.alten.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import es.alten.domain.Episode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Schema(name = "EpisodeSwaggerDTO", description = "Data transfer object for Swagger schemas. Episode")
@EqualsAndHashCode(callSuper = true)
@Data
public class EpisodeSwaggerDTO extends ElvisBaseDTO<Episode> {
    private static final long serialVersionUID = 1838376479439364474L;

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

    @Schema(description = "Episode characters")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private List<CharacterDTO> characters;
}

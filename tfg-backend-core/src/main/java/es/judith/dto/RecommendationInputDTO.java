package es.judith.dto;

import es.judith.domain.Recommendation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minidev.json.annotate.JsonIgnore;

@Schema(name = "RecommendationInputDTO", description = "Data transfer object for input: recommendation")
@EqualsAndHashCode(callSuper = true)
@Data
public class RecommendationInputDTO extends GenericDTO<Recommendation> {

    @JsonIgnore private Long id;
    @JsonIgnore private Long userSenderId;
    @NotNull private Long userReceiverId;
    @NotNull private Long showId;
}

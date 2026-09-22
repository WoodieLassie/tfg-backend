package es.judith.dto;

import es.judith.domain.Recommendation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Schema(name = "RecommendationDTO", description = "Data transfer object: recommendation")
@EqualsAndHashCode(callSuper = true)
@Data
public class RecommendationDTO extends GenericDTO<Recommendation> {

    @Serial
    private static final long serialVersionUID = 5658787834259233213L;

    @NotNull
    private Long id;
    @NotNull private UserProfileDTO userSender;
    @NotNull private UserProfileDTO userReceiver;
    @NotNull private ShowDTO show;
}

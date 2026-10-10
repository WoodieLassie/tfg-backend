package es.judith.dto.recommendation;

import es.judith.domain.Recommendation;
import es.judith.dto.GenericDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minidev.json.annotate.JsonIgnore;

import java.util.Objects;
import java.util.stream.Stream;

@Schema(name = "RecommendationInputDTO", description = "Data transfer object for input: recommendation")
@EqualsAndHashCode(callSuper = true)
@Data
public class RecommendationInputDTO extends GenericDTO<Recommendation> {

    @JsonIgnore private Long id;
    @JsonIgnore private Long userSenderId;
    @NotNull private Long userReceiverId;
    @NotNull private Long showId;

    public boolean allFieldsArePresent() {
        return Stream.of(this.userSenderId, this.userReceiverId, this.showId).allMatch(Objects::nonNull);
    }
}

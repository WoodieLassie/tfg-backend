package es.judith.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import es.judith.domain.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Schema(name = "ReviewInputDTO", description = "Data transfer object for input. Review")
@EqualsAndHashCode(callSuper = true)
@Data
public class ReviewInputDTO extends ElvisBaseDTO<Review> {
  @Serial private static final long serialVersionUID = 7844791754882313949L;

  @JsonIgnore private Long id;

  @NotNull private Integer rating;
  @NotNull private Long showId;
}

package es.judith.dto;

import es.judith.domain.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Schema(name = "ReviewDTO", description = "Data transfer object. Review")
@EqualsAndHashCode(callSuper = true)
@Data
public class ReviewDTO extends ElvisBaseDTO<Review> {
  @Serial private static final long serialVersionUID = 1895621387307881661L;

  @NotNull private Integer rating;
}

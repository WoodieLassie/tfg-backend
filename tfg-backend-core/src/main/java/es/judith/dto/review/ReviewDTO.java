package es.judith.dto.review;

import es.judith.domain.Review;
import es.judith.dto.GenericDTO;
import es.judith.dto.show.ShowDTO;
import es.judith.dto.show.ShowNoSeasonsDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Schema(name = "ReviewDTO", description = "Data transfer object. Review")
@EqualsAndHashCode(callSuper = true)
@Data
public class ReviewDTO extends GenericDTO<Review> {
  @Serial private static final long serialVersionUID = 1895621387307881661L;

  @NotNull private ShowNoSeasonsDTO show;
  @NotNull private Integer rating;
}

package es.judith.dto;

import es.judith.domain.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serial;

@Schema(name = "ReviewFilterDTO", description = "Review filter")
@EqualsAndHashCode(callSuper = true)
@Data
public class ReviewFilterDTO extends BaseFilterDTO<Review> {
  @Serial private static final long serialVersionUID = -5414268468633452990L;

  @Override
  public Specification obtainFilterSpecification() {
    return null;
  }
}

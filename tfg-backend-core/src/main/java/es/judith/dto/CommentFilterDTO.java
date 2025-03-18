package es.judith.dto;

import es.judith.domain.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serial;

@Schema(name = "CommentFilterDTO", description = "Comment filter")
@EqualsAndHashCode(callSuper = true)
@Data
public class CommentFilterDTO extends BaseFilterDTO<Comment> {
  @Serial private static final long serialVersionUID = 8858646088020652188L;

  @Override
  public Specification obtainFilterSpecification() {
    return null;
  }
}

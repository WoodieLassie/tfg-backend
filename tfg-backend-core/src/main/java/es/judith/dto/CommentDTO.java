package es.judith.dto;

import es.judith.domain.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Schema(name = "CommentDTO", description = "Data transfer object. Comment")
@EqualsAndHashCode(callSuper = true)
@Data
public class CommentDTO extends ElvisBaseDTO<Comment> {
  @Serial private static final long serialVersionUID = 1808854291344091870L;

  @NotNull private String text;
  @NotNull private String email;
}

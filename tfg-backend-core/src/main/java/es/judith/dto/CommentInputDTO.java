package es.judith.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import es.judith.domain.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Objects;
import java.util.stream.Stream;

@Schema(name = "CommentInputDTO", description = "Data transfer object for input. Comment")
@EqualsAndHashCode(callSuper = true)
@Data
public class CommentInputDTO extends ElvisBaseDTO<Comment> {
  @Serial private static final long serialVersionUID = -652917558111237079L;

  @JsonIgnore private Long id;

  @NotNull private String text;
  @JsonIgnore private Long userId;
  @NotNull private Long showId;

  public boolean allFieldsArePresent() {
    return Stream.of(this.text, this.showId).allMatch(Objects::nonNull);
  }
}

package es.judith.dto;

import es.judith.domain.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Schema(name = "CommentInputDTO", description = "Data transfer object for input. Comment")
@EqualsAndHashCode(callSuper = true)
@Data
public class CommentInputDTO extends ElvisBaseDTO<Comment> {
    @Serial private static final long serialVersionUID = -652917558111237079L;

    @NotNull private String text;
    @NotNull private Long showId;
}

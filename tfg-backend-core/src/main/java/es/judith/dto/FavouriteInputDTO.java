package es.judith.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import es.judith.domain.Favourite;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Objects;
import java.util.stream.Stream;

@Schema(name = "SeasonInputDTO", description = "Data transfer object for input. Favourite")
@EqualsAndHashCode(callSuper = true)
@Data
public class FavouriteInputDTO extends ElvisBaseDTO<Favourite> {
  @Serial private static final long serialVersionUID = 6767191369896423306L;

  @JsonIgnore private Long id;

  @JsonIgnore private Long userId;

  @Schema(description = "Show identification")
  @NotNull
  private Long showId;

  public boolean allFieldsArePresent() {
    return Stream.of(this.showId).allMatch(Objects::nonNull);
  }
}

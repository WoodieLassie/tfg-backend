package es.judith.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import es.judith.domain.Season;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Objects;
import java.util.stream.Stream;

@Schema(name = "SeasonInputDTO", description = "Data transfer object for input. Season")
@EqualsAndHashCode(callSuper = true)
@Data
public class SeasonInputDTO extends ElvisBaseDTO<Season> {
  @Serial private static final long serialVersionUID = -7833187139596920918L;

  @JsonIgnore private Long id;

  @Schema(description = "Season number")
  @NotNull
  private Integer seasonNum;

  @Schema(description = "Season description")
  @NotNull
  private String description;

  @Schema(description = "Season identification")
  @NotNull
  private Long showId;

  public boolean allFieldsArePresent() {
    return Stream.of(this.seasonNum, this.description, this.showId).allMatch(Objects::nonNull);
  }
}

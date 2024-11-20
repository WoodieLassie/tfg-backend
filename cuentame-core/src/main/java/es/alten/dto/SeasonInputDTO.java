package es.alten.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import es.alten.domain.Season;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Objects;
import java.util.stream.Stream;

@Schema(name = "SeasonInputDTO", description = "Data transfer object for input. Season")
@EqualsAndHashCode(callSuper = true)
@Data
public class SeasonInputDTO extends ElvisBaseDTO<Season> {
  private static final long serialVersionUID = -7833187139596920918L;

  @JsonIgnore private Long id;

  @Schema(description = "Season number")
  @NotNull
  private Integer seasonNum;

  @Schema(description = "Season description")
  @NotNull
  private String description;

  public boolean allFieldsArePresent() {
    return Stream.of(this.seasonNum, this.description).allMatch(Objects::nonNull);
  }
}

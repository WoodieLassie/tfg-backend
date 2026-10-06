package es.judith.dto.favourite;

import es.judith.domain.Favourite;
import es.judith.dto.GenericDTO;
import es.judith.dto.show.ShowDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Schema(name = "FavouriteDTO", description = "Data transfer object. Favourite")
@EqualsAndHashCode(callSuper = true)
@Data
public class FavouriteDTO extends GenericDTO<Favourite> {
  @Serial private static final long serialVersionUID = -5526229818088298889L;

  @Schema(description = "Favourite identification")
  @NotNull
  private Long id;

  @Schema(description = "Show identification")
  @NotNull
  private ShowDTO show;
}

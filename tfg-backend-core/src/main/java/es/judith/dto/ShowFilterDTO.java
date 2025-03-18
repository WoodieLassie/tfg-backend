package es.judith.dto;

import es.judith.domain.Show;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serial;

@Schema(name = "ShowFilterDTO", description = "Show filter")
@EqualsAndHashCode(callSuper = true)
@Data
public class ShowFilterDTO extends BaseFilterDTO<Show> {
  @Serial private static final long serialVersionUID = -6256982880514697655L;

  @Override
  public Specification obtainFilterSpecification() {
    return null;
  }
}

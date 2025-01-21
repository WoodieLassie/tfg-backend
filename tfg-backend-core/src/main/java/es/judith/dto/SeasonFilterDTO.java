package es.judith.dto;

import es.judith.domain.Season;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.criteria.Predicate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serial;
import java.util.List;

@Schema(name = "SeasonFilterDTO", description = "Season filter")
@EqualsAndHashCode(callSuper = true)
@Data
public class SeasonFilterDTO extends BaseFilterDTO<Season> {
  @Serial private static final long serialVersionUID = 1400069111102606308L;

  private Integer seasonNum;
  private String description;
  private List<EpisodeDTO> episodes;

  @Override
  public Specification<Season> obtainFilterSpecification() {
    return (root, query, cb) -> {
      Predicate predicate = cb.and();
      if (seasonNum != null) {
        predicate = cb.and(predicate, cb.like(root.get("email"), "%" + this.seasonNum + "%"));
      }
      return predicate;
    };
  }
}

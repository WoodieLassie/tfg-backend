package es.alten.dto;

import es.alten.domain.Episode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.criteria.Predicate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@Schema(name = "EpisodeFilterDTO", description = "Episode filter")
@EqualsAndHashCode(callSuper = true)
@Data
public class EpisodeFilterDTO extends BaseFilterDTO<Episode> {
    @Serial private static final long serialVersionUID = -7992544375492279688L;

    private Integer episodeNum;
    private String title;
    private String summary;
    private SeasonDTO season;
    private List<CharacterDTO> characters;

    @Override
    public Specification<Episode> obtainFilterSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (season != null ) {
                predicates.add(cb.like(root.get("season"), "%" + this.season + "%"));
            }
            if (StringUtils.isNotBlank(title)) {
                predicates.add(cb.like(root.get("title"), "%" + this.title.toLowerCase() + "%"));
            }
            if (episodeNum != null) {
                predicates.add(cb.like(root.get("episodeNum"), "%" + this.episodeNum + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

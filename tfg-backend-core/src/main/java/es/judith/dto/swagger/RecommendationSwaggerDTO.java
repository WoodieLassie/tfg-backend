package es.judith.dto.swagger;

import es.judith.domain.Recommendation;
import es.judith.dto.GenericDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Map;

@Schema(name = "RecommendationSwaggerDTO", description = "Data transfer object for swagger: recommendation")
@EqualsAndHashCode(callSuper = true)
@Data
public class RecommendationSwaggerDTO extends GenericDTO<Recommendation> {
    private Map<Long, ArrayList> result;
}

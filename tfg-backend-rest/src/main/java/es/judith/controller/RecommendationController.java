package es.judith.controller;

import es.judith.domain.Recommendation;
import es.judith.dto.RecommendationInputDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public interface RecommendationController extends Serializable {
    ResponseEntity<Map<Long, ArrayList>> findAllSentRecommendations();
    ResponseEntity<Map<Long, ArrayList>> findAllReceivedRecommendations();
    ResponseEntity<Recommendation> sendRecommendation(RecommendationInputDTO recommendationDTO);
    ResponseEntity<Recommendation> delete(Long recommendationId);
}

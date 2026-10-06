package es.judith.controller;

import es.judith.domain.Recommendation;
import es.judith.dto.recommendation.RecommendationInputDTO;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public interface RecommendationController extends Serializable {
    ResponseEntity<Map<Long, ArrayList>> findAllSentRecommendations();
    ResponseEntity<Map<Long, ArrayList>> findAllReceivedRecommendations();
    ResponseEntity<Recommendation> sendRecommendation(RecommendationInputDTO recommendationDTO);
    ResponseEntity<Recommendation> delete(Long recommendationId);
}

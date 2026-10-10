package es.judith.controller;

import es.judith.domain.Recommendation;
import es.judith.dto.recommendation.RecommendationInputDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public interface RecommendationController extends Serializable {
    ResponseEntity<Page<Map.Entry<Long, ArrayList>>> findAllSentRecommendations(Integer page, Integer size, Pageable pageable);
    ResponseEntity<Page<Map.Entry<Long, ArrayList>>> findAllReceivedRecommendations(Integer page, Integer size, Pageable pageable);
    ResponseEntity<Recommendation> sendRecommendation(RecommendationInputDTO recommendationDTO);
    ResponseEntity<Recommendation> delete(Long recommendationId);
}

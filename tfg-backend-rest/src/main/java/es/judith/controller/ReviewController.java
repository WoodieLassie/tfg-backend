package es.judith.controller;

import es.judith.domain.Review;
import es.judith.dto.review.ReviewDTO;
import es.judith.dto.review.ReviewInputDTO;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public interface ReviewController extends Serializable {
    ResponseEntity<HashMap<String, Double>> findAverageRatingByShow(Long showId);
    ResponseEntity<List<ReviewDTO>> findAllByUserId(Long userId);
    ResponseEntity<Review> add(ReviewInputDTO reviewDTO);
    ResponseEntity<Review> delete(Long id);
}

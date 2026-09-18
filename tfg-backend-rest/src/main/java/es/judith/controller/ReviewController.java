package es.judith.controller;

import es.judith.domain.Review;
import es.judith.dto.ReviewInputDTO;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.HashMap;

public interface ReviewController extends Serializable {
    ResponseEntity<HashMap<String, Double>> findAll(Long showId);
    ResponseEntity<Review> add(ReviewInputDTO reviewDTO);
    ResponseEntity<Review> delete(Long id);
}

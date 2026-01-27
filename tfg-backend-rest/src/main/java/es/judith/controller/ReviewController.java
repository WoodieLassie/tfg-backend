package es.judith.controller;

import es.judith.domain.Review;
import es.judith.dto.ReviewInputDTO;
import es.judith.rest.BaseController;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;

public interface ReviewController extends BaseController {
    ResponseEntity<HashMap<String, Double>> findAll(Long showId);
    ResponseEntity<Review> add(ReviewInputDTO reviewDTO);
    ResponseEntity<Review> delete(Long id);
}

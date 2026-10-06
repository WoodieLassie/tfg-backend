package es.judith.bo;

import es.judith.domain.Review;
import es.judith.dto.review.ReviewDTO;

import java.util.List;

public interface ReviewBO extends GenericBO<Review, Long> {
    List<Review> findAllByShowId(Long showId);
    List<Review> findAllByUserId(Long userId);
    Review checkIfUserReviewInShow(Long showId, Long userId);
    Double calculateAverageRating(Long showId);
    ReviewDTO convertToDTO(Review review);
}

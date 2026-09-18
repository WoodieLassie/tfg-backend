package es.judith.bo;

import es.judith.domain.Review;

import java.util.List;

public interface ReviewBO extends GenericBO<Review, Long> {
    List<Review> findAllByShowId(Long showId);
    Review checkIfUserReviewInShow(Long showId, Long userId);
}

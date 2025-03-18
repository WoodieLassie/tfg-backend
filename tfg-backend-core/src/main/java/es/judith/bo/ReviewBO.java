package es.judith.bo;

import es.judith.domain.QReview;
import es.judith.domain.Review;
import es.judith.dto.ReviewFilterDTO;

import java.util.List;

public interface ReviewBO extends GenericCRUDService<Review, Long, QReview, ReviewFilterDTO> {
    List<Review> findByShowId(Long showId);
}

package es.judith.bo.impl;

import es.judith.bo.ReviewBO;
import es.judith.dao.ReviewRepository;
import es.judith.domain.QReview;
import es.judith.domain.Review;
import es.judith.dto.ReviewFilterDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReviewBOImpl extends ElvisGenericCRUDServiceImpl<
        Review, Long, QReview, ReviewFilterDTO, ReviewRepository>
        implements ReviewBO {

    public ReviewBOImpl(ReviewRepository repository) {
        super(repository);
    }

    @Transactional(readOnly = true)
    public List<Review> findAllByShowId(Long showId) {
        return repository.findByShow(showId);
    }
    @Transactional(readOnly = true)
    public Review checkIfUserReviewInShow(Long showId, Long userId) {
        return repository.checkIfUserReviewInShow(showId, userId);
    }
}

package es.judith.bo.impl;

import es.judith.bo.ReviewBO;
import es.judith.dao.ReviewRepository;
import es.judith.domain.Review;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReviewBOImpl extends GenericBOImpl<
        Review, Long, ReviewRepository>
        implements ReviewBO {

    public ReviewBOImpl(ReviewRepository repository) {
        super(repository);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> findAllByShowId(Long showId) {
        return repository.findByShow(showId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> findAllByUserId(Long userId) {
        return repository.findByUser(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Review checkIfUserReviewInShow(Long showId, Long userId) {
        return repository.checkIfUserReviewInShow(showId, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Double calculateAverageRating(Long showId) {
        List<Review> reviewList = repository.findByShow(showId);
        Double totalReviewScore = 0.0;
        for (Review review : reviewList) {
            totalReviewScore += review.getRating();
        }
        totalReviewScore = totalReviewScore / reviewList.size();
        String totalReviewScoreTruncated = totalReviewScore.toString().substring(0,3);
        totalReviewScore = Double.valueOf(totalReviewScoreTruncated);
        return totalReviewScore;
    }
}

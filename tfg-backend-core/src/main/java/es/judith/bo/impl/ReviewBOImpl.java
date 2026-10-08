package es.judith.bo.impl;

import es.judith.bo.ReviewBO;
import es.judith.bo.ShowBO;
import es.judith.dao.ReviewRepository;
import es.judith.domain.Review;
import es.judith.domain.Show;
import es.judith.dto.review.ReviewDTO;
import es.judith.dto.show.ShowNoSeasonsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serial;
import java.util.List;

@Service
@Transactional
public class ReviewBOImpl extends GenericBOImpl<
        Review, Long, ReviewRepository>
        implements ReviewBO {

    private final transient ShowBO showBO;
    @Serial
    private static final long serialVersionUID = -8987230218466264611L;
    private static final Logger LOG = LoggerFactory.getLogger(ReviewBOImpl.class);

    public ReviewBOImpl(ReviewRepository repository, ShowBO showBO) {
        super(repository);
        this.showBO = showBO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> findAllByShowId(Long showId) {
        LOG.debug("ReviewBOImpl: findAllByShowId");
        return repository.findByShow(showId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> findAllByUserId(Long userId) {
        LOG.debug("ReviewBOImpl: findAllByUserId");
        return repository.findByUser(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Review checkIfUserReviewInShow(Long showId, Long userId) {
        LOG.debug("ReviewBOImpl: checkIfUserReviewInShow");
        return repository.checkIfUserReviewInShow(showId, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Double calculateAverageRating(Long showId) {
        LOG.debug("ReviewBOImpl: calculateAverageRating");
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

    @Override
    public ReviewDTO convertToDTO(Review userReview) {
        LOG.debug("ReviewBOImpl: convertToDTO");
        ReviewDTO convertedUserReview = new ReviewDTO();
        ShowNoSeasonsDTO convertedShowFromReview = new ShowNoSeasonsDTO();
        convertedUserReview.loadFromDomain(userReview);
        Show showFromReview = showBO.findOne(userReview.getShow().getId());
        convertedShowFromReview.loadFromDomain(showFromReview);
        convertedUserReview.setShow(convertedShowFromReview);
        return convertedUserReview;
    }
}

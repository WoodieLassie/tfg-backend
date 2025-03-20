package es.judith.dao;

import es.judith.domain.QReview;
import es.judith.domain.Review;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository
    extends ElvisBaseRepository<Review, Long, QReview>,
        JpaSpecificationExecutor<Review>,
        QuerydslPredicateExecutor<Review>,
        QuerydslBinderCustomizer<QReview> {
    @Query(nativeQuery = true, value = "SELECT r.* FROM reviews r WHERE r.show_id = :showId")
    List<Review> findByShow(@Param("showId") Long showId);
    @Query(nativeQuery = true, value = "SELECT r.* FROM reviews r WHERE r.show_id = :showId AND r.create_user_id = :userId")
    Review checkIfUserReviewInShow(@Param("showId") Long showId, @Param("userId") Long userId);
}

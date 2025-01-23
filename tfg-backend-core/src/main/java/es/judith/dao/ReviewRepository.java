package es.judith.dao;

import es.judith.domain.QReview;
import es.judith.domain.Review;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository
    extends ElvisBaseRepository<Review, Long, QReview>,
        JpaSpecificationExecutor<Review>,
        QuerydslPredicateExecutor<Review>,
        QuerydslBinderCustomizer<QReview> {
  @Modifying
  @Query(
      value =
          "UPDATE reviews r SET r.rating = :rating WHERE r.show_id = :showId AND r.created_user_id = :userId",
      nativeQuery = true)
  Review update(
      @Param("userId") Long userId, @Param("showId") Long showId, @Param("rating") Integer rating);
}

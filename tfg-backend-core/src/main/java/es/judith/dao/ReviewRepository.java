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
    @Query("SELECT r FROM Review r WHERE r.show = :showId")
    List<Review> findByShow(@Param("showId") Long showId);
}

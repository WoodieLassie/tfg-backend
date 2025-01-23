package es.judith.dao;

import es.judith.domain.Comment;
import es.judith.domain.QComment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository
    extends ElvisBaseRepository<Comment, Long, QComment>,
        JpaSpecificationExecutor<Comment>,
        QuerydslPredicateExecutor<Comment>,
        QuerydslBinderCustomizer<QComment> {
  @Query(
      value =
          "SELECT c.*, u.email FROM comments c, users u WHERE c.created_user_id = u.id AND c.show_id = :showId",
      nativeQuery = true)
  List<Comment> findAll(@Param("showId") Long showId);
}

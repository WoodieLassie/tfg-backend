package es.judith.dao;

import es.judith.domain.Comment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository
    extends ElvisBaseRepository<Comment, Long>,
        JpaSpecificationExecutor<Comment> {
  @Query(
      value =
          "SELECT c.*, u.email FROM comments c LEFT JOIN users u ON c.create_user_id = u.id WHERE c.show_id = :showId",
      nativeQuery = true)
  List<Object[]> findAll(@Param("showId") Long showId);
}

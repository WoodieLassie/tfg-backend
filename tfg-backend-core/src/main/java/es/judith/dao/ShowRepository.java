package es.judith.dao;

import es.judith.domain.QShow;
import es.judith.domain.Show;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShowRepository extends ElvisBaseRepository<Show, Long, QShow>,
        JpaSpecificationExecutor<Show>,
        QuerydslPredicateExecutor<Show>,
        QuerydslBinderCustomizer<QShow> {
    @Query("SELECT s FROM Show s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY s.id")
    List<Show> findAllByName(@Param("name") String name);
}

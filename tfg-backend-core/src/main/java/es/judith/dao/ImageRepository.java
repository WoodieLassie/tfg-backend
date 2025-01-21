package es.judith.dao;

import es.judith.domain.Image;
import es.judith.domain.QImage;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends ElvisBaseRepository<Image, Long, QImage>,
        JpaSpecificationExecutor<Image>,
        QuerydslPredicateExecutor<Image>,
        QuerydslBinderCustomizer<QImage> {
    @Query("SELECT i FROM Image i WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Image> findByName(@Param("name") String name);
}
